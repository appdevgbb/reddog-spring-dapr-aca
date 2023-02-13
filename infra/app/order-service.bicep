param name string
param location string = resourceGroup().location
param tags object = {}

param applicationInsightsName string
param containerAppsEnvironmentName string
param containerRegistryName string
param imageName string = ''
param serviceName string = 'order-service'

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
    targetPort: 8702
    enableDapr: true
    daprAppPort: 8702
    daprAppId: serviceName
    external: true
  }
}

resource applicationInsights 'Microsoft.Insights/components@2020-02-02' existing = {
  name: applicationInsightsName
}


output SERVICE_ORDER_IDENTITY_PRINCIPAL_ID string = app.outputs.identityPrincipalId
output SERVICE_ORDER_NAME string = app.outputs.name
output SERVICE_ORDER_URI string = app.outputs.uri
output SERVICE_ORDER_IMAGE_NAME string = app.outputs.imageName
