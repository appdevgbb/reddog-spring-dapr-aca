param name string
param location string = resourceGroup().location
param tags object = {}

param logAnalyticsWorkspaceName string
param appInsightsInstrumentationKey string

resource containerAppsEnvironment 'Microsoft.App/managedEnvironments@2022-10-01' = {
  name: name
  location: location
  tags: tags
  sku: {
    name: 'Consumption'
  }
  properties: {
    daprAIInstrumentationKey: appInsightsInstrumentationKey
    appLogsConfiguration: {
      destination: 'log-analytics'
      
      logAnalyticsConfiguration: {
        customerId: logAnalyticsWorkspace.properties.customerId
        sharedKey: logAnalyticsWorkspace.listKeys().primarySharedKey
      }
    }
  }
}

resource logAnalyticsWorkspace 'Microsoft.OperationalInsights/workspaces@2022-10-01' existing = {
  name: logAnalyticsWorkspaceName
}

output name string = containerAppsEnvironment.name
