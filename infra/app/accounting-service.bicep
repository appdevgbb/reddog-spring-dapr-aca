param name string
param location string = resourceGroup().location
param tags object = {}

param applicationInsightsName string
param containerAppsEnvironmentName string
param containerRegistryName string
param imageName string = ''
param serviceName string = 'accounting-service'
param serviceBusNamespaceName string
param cosmosAccountName string
param appPort int = 8707

resource serviceBus 'Microsoft.ServiceBus/namespaces@2021-06-01-preview' existing = {
  name: serviceBusNamespaceName
}

resource serviceBusAuthRules 'Microsoft.ServiceBus/namespaces/AuthorizationRules@2022-01-01-preview' existing = {
  name: 'RootManageSharedAccessKey'
  parent: serviceBus
}

resource cosmosAccount 'Microsoft.DocumentDB/databaseAccounts@2022-08-15' existing = {
  name: cosmosAccountName
}

var scaleRules = [
  {
    name: 'service-bus-scale-rule'
    custom: {
      type: 'azure-servicebus'
      metadata: {
        topicName: 'orders'
        subscriptionName: 'accounting-service'
        messageCount: '10'
      }
      auth: [
        {
          secretRef: 'sb-root-connectionstring'
          triggerParameter: 'connection'
        }
      ]
    }
  }
  {
    name: 'http-rule'
    http: {
      metadata: {
          concurrentRequests: '100'
      }
    }
  }
]

var secrets = [
  {
    name: 'sb-root-connectionstring'
    value: serviceBusAuthRules.listKeys().primaryConnectionString
  }
]
var probes = [
  {
    type: 'readiness'
    httpGet: {
      path: '/actuator/health/readiness'
      port: appPort
    }
    timeoutSeconds: 10
    failureThreshold: 10
    periodSeconds: 10
  }
  {
    type: 'liveness'
    httpGet: {
      path: '/actuator/health/liveness'
      port: appPort
    }
    timeoutSeconds: 10
    successThreshold: 1
    failureThreshold: 10
    periodSeconds: 10
  }
  {
    type: 'startup'
    httpGet: {
      path: '/actuator/health/readiness'
      port: appPort
    }
    timeoutSeconds: 10
    failureThreshold: 6
    periodSeconds: 10
    initialDelaySeconds: 10
  }
]

module app '../core/host/container-app.bicep' = {
  name: '${serviceName}-container-app-module'
  params: {
    name: name
    location: location
    tags: union(tags, { 'azd-service-name': serviceName })
    containerAppsEnvironmentName: containerAppsEnvironmentName
    containerRegistryName: containerRegistryName
    containerCpuCoreCount: '1.0'
    containerMemory: '2.0Gi'
    env: [
      {
        name: 'APPLICATIONINSIGHTS_CONNECTION_STRING'
        value: applicationInsights.properties.ConnectionString
      }
      {
        name: 'AZURECOSMOSDBKEY'
        value: cosmosAccount.listKeys().primaryMasterKey
      }
      {
        name: 'AZURECOSMOSDBSECONDARYKEY'
        value: cosmosAccount.listKeys().secondaryMasterKey
      }
      {
        name: 'AZURECOSMOSDBDATABASENAME'
        value: 'accounting'
      }
      {
        name: 'AZURECOSMOSDBURI'
        value: cosmosAccount.properties.documentEndpoint
      }
    ]
    imageName: !empty(imageName) ? imageName : 'nginx:latest'
    targetPort: appPort
    enableDapr: true
    daprAppPort: appPort
    daprAppId: serviceName
    scaleRules: scaleRules
    secrets: secrets
    external: true
    probes: probes
  }
}

resource applicationInsights 'Microsoft.Insights/components@2020-02-02' existing = {
  name: applicationInsightsName
}


output SERVICE_ACCOUNTING_IDENTITY_PRINCIPAL_ID string = app.outputs.identityPrincipalId
output SERVICE_ACCOUNTING_NAME string = app.outputs.name
output SERVICE_ACCOUNTING_URI string = app.outputs.uri
