param name string
param location string = resourceGroup().location
param tags object = {}

param applicationInsightsName string
param containerAppsEnvironmentName string
param containerRegistryName string
param imageName string = ''
param serviceName string = 'order-service'
param appPort int = 8702

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
    ]
    imageName: !empty(imageName) ? imageName : 'nginx:latest'
    targetPort: appPort
    enableDapr: true
    daprAppPort: appPort
    daprAppId: serviceName
    external: true
    probes: probes
  }
}

resource applicationInsights 'Microsoft.Insights/components@2020-02-02' existing = {
  name: applicationInsightsName
}


output SERVICE_ORDER_IDENTITY_PRINCIPAL_ID string = app.outputs.identityPrincipalId
output SERVICE_ORDER_NAME string = app.outputs.name
output SERVICE_ORDER_URI string = app.outputs.uri
