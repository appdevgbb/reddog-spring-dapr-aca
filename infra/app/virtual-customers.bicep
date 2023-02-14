param name string
param location string = resourceGroup().location
param tags object = {}

param applicationInsightsName string
param containerAppsEnvironmentName string
param containerRegistryName string
param imageName string = ''
param serviceName string = 'virtual-customers'

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
    targetPort: 8701
    enableDapr: true
    daprAppPort: 8701
    daprAppId: serviceName
    minReplicas: 1
    external: true
  }
}

resource applicationInsights 'Microsoft.Insights/components@2020-02-02' existing = {
  name: applicationInsightsName
}


output SERVICE_VIRTUAL_CUSTOMERS_IDENTITY_PRINCIPAL_ID string = app.outputs.identityPrincipalId
output SERVICE_VIRTUAL_CUSTOMERS_NAME string = app.outputs.name
output SERVICE_VIRTUAL_CUSTOMERS_URI string = app.outputs.uri
