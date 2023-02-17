param name string
param location string = resourceGroup().location
param tags object = {}

param applicationInsightsName string
param containerAppsEnvironmentName string
param containerRegistryName string
param imageName string = ''
param serviceName string = 'ui'
param appPort int = 3000
param virtualCustomersUri string
param ordersUri string
param accountingUri string



var scaleRules = [
  {
    name: 'http-rule'
    http: {
      metadata: {
          concurrentRequests: '100'
      }
    }
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
        name: 'VIRTUAL_CUSTOMERS_URL'
        value: virtualCustomersUri
      }
      {
        name: 'ORDERS_URL'
        value: ordersUri
      }
      {
        name: 'WORKER_URL'
        value: ordersUri
      }
      {
        name: 'ACCOUNTING_URL'
        value: accountingUri
      }
      {
        name: 'OPENAI_URL'
        value: ''
      }
    ]
    imageName: !empty(imageName) ? imageName : 'nginx:latest'
    targetPort: appPort
    enableDapr: true
    daprAppPort: appPort
    daprAppId: serviceName
    scaleRules: scaleRules
    external: true
  }
}

resource applicationInsights 'Microsoft.Insights/components@2020-02-02' existing = {
  name: applicationInsightsName
}


output SERVICE_UI_IDENTITY_PRINCIPAL_ID string = app.outputs.identityPrincipalId
output SERVICE_UI_NAME string = app.outputs.name
output SERVICE_UI_URI string = app.outputs.uri
