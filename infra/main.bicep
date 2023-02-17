targetScope = 'subscription'

@minLength(1)
@maxLength(64)
@description('Name of the the environment which is used to generate a short unique hash used in all resources.')
param environmentName string

@minLength(1)
@description('Primary location for all resources')
param location string

param applicationInsightsName string = ''
param containerAppsEnvironmentName string = ''
param containerRegistryName string = ''
param cosmosAccountName string = ''
param cosmosDatabaseName string = ''
param logAnalyticsName string = ''
param resourceGroupName string = ''
param storageAccountName string = ''
param serviceBusNamespaceName string = ''
param redisName string = ''
param openAIAccountName string = ''


@description('The image name for the order service')
param orderServiceImageName string = ''
param orderServiceContainerAppName string = ''

@description('The image name for the makeline service')
param makelineServiceImageName string = ''
param makelineServiceContainerAppName string = ''

@description('The image name for the loyalty service')
param loyaltyServiceImageName string = ''
param loyaltyServiceContainerAppName string = ''

@description('The image name for the receipt generation service')
param receiptGenerationServiceImageName string = ''
param receiptGenerationServiceContainerAppName string = ''


@description('The image name for the accounting service')
param accountingServiceImageName string = ''
param accountingServiceContainerAppName string = ''

@description('The image name for the virtual worker')
param virtualWorkerImageName string = ''
param virtualWorkerContainerAppName string = ''

@description('The image name for the virtual customer')
param virtualCustomerImageName string = ''
param virtualCustomerContainerAppName string = ''

@description('The image name for the virtual customer')
param uiImageName string = ''
param uiContainerAppName string = ''



var abbrs = loadJsonContent('./abbreviations.json')
var resourceToken = toLower(uniqueString(subscription().id, environmentName, location))
var tags = { 'azd-env-name': environmentName }

// Organize resources in a resource group
resource rg 'Microsoft.Resources/resourceGroups@2021-04-01' = {
  name: !empty(resourceGroupName) ? resourceGroupName : '${abbrs.resourcesResourceGroups}${environmentName}'
  location: location
  tags: tags
}

// Container apps host (including container registry)
module containerApps './core/host/container-apps.bicep' = {
  name: 'container-apps'
  scope: rg
  params: {
    name: 'app'
    containerAppsEnvironmentName: !empty(containerAppsEnvironmentName) ? containerAppsEnvironmentName : '${abbrs.appManagedEnvironments}${resourceToken}'
    containerRegistryName: !empty(containerRegistryName) ? containerRegistryName : '${abbrs.containerRegistryRegistries}${resourceToken}'
    appInsightsInstrumentationKey: monitoring.outputs.applicationInsightsInstrumentationKey
    location: location
    logAnalyticsWorkspaceName: monitoring.outputs.logAnalyticsWorkspaceName
  }
}

// The application database
module cosmos './app/db.bicep' = {
  name: 'cosmos'
  scope: rg
  params: {
    accountName: !empty(cosmosAccountName) ? cosmosAccountName : '${abbrs.documentDBDatabaseAccounts}${resourceToken}'
    databaseName: cosmosDatabaseName
    location: location
    tags: tags
  }
}




// Monitor application with Azure Monitor
module monitoring './core/monitor/monitoring.bicep' = {
  name: 'monitoring'
  scope: rg
  params: {
    location: location
    tags: tags
    logAnalyticsName: !empty(logAnalyticsName) ? logAnalyticsName : '${abbrs.operationalInsightsWorkspaces}${resourceToken}'
    applicationInsightsName: !empty(applicationInsightsName) ? applicationInsightsName : '${abbrs.insightsComponents}${resourceToken}'
  }
}


module serviceBus './core/messaging/servicebus.bicep' = {
  name: '${deployment().name}--servicebus'
  scope: rg
  params: {
    name: !empty(serviceBusNamespaceName) ? serviceBusNamespaceName : '${abbrs.serviceBusNamespaces}${resourceToken}'
    location: location
  }
}

module redis './core/database/redis/redis.bicep' = {
  name: '${deployment().name}--rediscache'
  scope: rg
  params: {
    name: !empty(redisName) ? redisName : '${abbrs.cacheRedis}${resourceToken}'
    location: location
  }
}

module storage './core/storage/storage-account.bicep' = {
  name: 'storage'
  scope: rg
  params: {
      name: !empty(storageAccountName) ? storageAccountName : '${abbrs.storageStorageAccounts}${resourceToken}'
      location: location
      tags: tags
      containers: [
        {
          name: 'receipts'
        }
      ]
  }
}

module daprBindingReceipt './core/dapr-components/binding-receipt.bicep' = {
  name: '${deployment().name}--dapr-binding-receipt'
  scope: rg
  params: {
    containerAppsEnvName: containerApps.outputs.environmentName
    storageAccountName: storage.outputs.name
  }
}

module daprBindingVirtualWorker './core/dapr-components/binding-virtualworker.bicep' = {
  name: '${deployment().name}--dapr-binding-virtualworker'
  scope: rg
  params: {
    containerAppsEnvName: containerApps.outputs.environmentName
  }
}

module daprPubsub './core/dapr-components/pubsub.bicep' = {
  name: '${deployment().name}--dapr-pubsub'
  scope: rg
  params: {
    containerAppsEnvName: containerApps.outputs.environmentName
    serviceBusNamespaceName: serviceBus.outputs.sbName
  }
}

module daprStateLoyalty './core/dapr-components/state-loyalty.bicep' = {
  name: '${deployment().name}--dapr-state-loyalty'
  scope: rg
  params: {
    containerAppsEnvName: containerApps.outputs.environmentName
    cosmosAccountName: cosmos.outputs.accountName
    cosmosDatabaseName: cosmos.outputs.databaseName
    cosmosCollectionName: cosmos.outputs.loyaltyContainerName
  }
}

module daprStateProducts './core/dapr-components/state-products.bicep' = {
  name: '${deployment().name}--dapr-state-products'
  scope: rg
  params: {
    containerAppsEnvName: containerApps.outputs.environmentName
    cosmosAccountName: cosmos.outputs.accountName
    cosmosDatabaseName: cosmos.outputs.databaseName
    cosmosCollectionName: cosmos.outputs.productsContainerName
  }
}

module daprStateMakeline './core/dapr-components/state-makeline.bicep' = {
  name: '${deployment().name}--dapr-state-makeline'
  scope: rg
  params: {
    containerAppsEnvName: containerApps.outputs.environmentName
    cosmosAccountName: cosmos.outputs.accountName
    cosmosDatabaseName: cosmos.outputs.databaseName
    cosmosCollectionName: cosmos.outputs.ordersContainerName
  }
}


// Order service backend
module orderService './app/order-service.bicep' = {
  name: 'order-service'
  scope: rg
  params: {
    name: !empty(orderServiceContainerAppName) ? orderServiceContainerAppName : '${abbrs.appContainerApps}order-${resourceToken}'
    location: location
    imageName: orderServiceImageName
    applicationInsightsName: monitoring.outputs.applicationInsightsName
    containerAppsEnvironmentName: containerApps.outputs.environmentName
    containerRegistryName: containerApps.outputs.registryName
  }
  dependsOn: [
    serviceBus
    daprPubsub
    daprStateProducts
  ]
}

// Makeline service backend
module makeLineService './app/makeline-service.bicep' = {
  name: 'makeline-service'
  scope: rg
  params: {
    name: !empty(makelineServiceContainerAppName) ? makelineServiceContainerAppName : '${abbrs.appContainerApps}makeline-${resourceToken}'
    location: location
    imageName: makelineServiceImageName
    applicationInsightsName: monitoring.outputs.applicationInsightsName
    containerAppsEnvironmentName: containerApps.outputs.environmentName
    containerRegistryName: containerApps.outputs.registryName
    serviceBusNamespaceName: serviceBus.outputs.sbName
  }
  dependsOn: [
    serviceBus
    redis
    daprPubsub
    daprStateMakeline
  ]
}

// Loyalty service backend
module loyaltyService './app/loyalty-service.bicep' = {
  name: 'loyalty-service'
  scope: rg
  params: {
    name: !empty(loyaltyServiceContainerAppName) ? loyaltyServiceContainerAppName : '${abbrs.appContainerApps}loyalty-${resourceToken}'
    location: location
    imageName: loyaltyServiceImageName
    applicationInsightsName: monitoring.outputs.applicationInsightsName
    containerAppsEnvironmentName: containerApps.outputs.environmentName
    containerRegistryName: containerApps.outputs.registryName
    serviceBusNamespaceName: serviceBus.outputs.sbName
  }
  dependsOn: [
    serviceBus
    daprPubsub
    daprStateLoyalty
  ]
}

// Loyalty service backend
module receiptGenerationService './app/receipt-generation-service.bicep' = {
  name: 'receipt-generation-service'
  scope: rg
  params: {
    name: !empty(receiptGenerationServiceContainerAppName) ? receiptGenerationServiceContainerAppName : '${abbrs.appContainerApps}receipt-${resourceToken}'
    location: location
    imageName: receiptGenerationServiceImageName
    applicationInsightsName: monitoring.outputs.applicationInsightsName
    containerAppsEnvironmentName: containerApps.outputs.environmentName
    containerRegistryName: containerApps.outputs.registryName
    serviceBusNamespaceName: serviceBus.outputs.sbName
  }
  dependsOn: [
    serviceBus
    daprPubsub
    daprBindingReceipt
  ]
}

// Accounting service backend
module accountingService './app/accounting-service.bicep' = {
  name: 'accounting-service'
  scope: rg
  params: {
    name: !empty(accountingServiceContainerAppName) ? accountingServiceContainerAppName : '${abbrs.appContainerApps}accounting-${resourceToken}'
    location: location
    imageName: accountingServiceImageName
    applicationInsightsName: monitoring.outputs.applicationInsightsName
    containerAppsEnvironmentName: containerApps.outputs.environmentName
    containerRegistryName: containerApps.outputs.registryName
    serviceBusNamespaceName: serviceBus.outputs.sbName
    cosmosAccountName: cosmos.outputs.accountName
  }
  dependsOn: [
    serviceBus
    daprPubsub
  ]
}

// Accounting service backend
module virtualWorker './app/virtual-worker.bicep' = {
  name: 'virtual-worker'
  scope: rg
  params: {
    name: !empty(virtualWorkerContainerAppName) ? virtualWorkerContainerAppName : '${abbrs.appContainerApps}virtualworker-${resourceToken}'
    location: location
    imageName: virtualWorkerImageName
    applicationInsightsName: monitoring.outputs.applicationInsightsName
    containerAppsEnvironmentName: containerApps.outputs.environmentName
    containerRegistryName: containerApps.outputs.registryName
  }
  dependsOn: [
    makeLineService
    daprBindingVirtualWorker
  ]
}

// Accounting service backend
module virtualCustomer './app/virtual-customers.bicep' = {
  name: 'virtual-customers'
  scope: rg
  params: {
    name: !empty(virtualCustomerContainerAppName) ? virtualCustomerContainerAppName : '${abbrs.appContainerApps}virtualcustomer-${resourceToken}'
    location: location
    imageName: virtualCustomerImageName
    applicationInsightsName: monitoring.outputs.applicationInsightsName
    containerAppsEnvironmentName: containerApps.outputs.environmentName
    containerRegistryName: containerApps.outputs.registryName
  }
  dependsOn: [
    orderService
  ]
}

// Order service backend
module ui './app/ui.bicep' = {
  name: 'ui'
  scope: rg
  params: {
    name: !empty(uiContainerAppName) ? uiContainerAppName : '${abbrs.appContainerApps}ui-${resourceToken}'
    location: location
    imageName: uiImageName
    applicationInsightsName: monitoring.outputs.applicationInsightsName
    containerAppsEnvironmentName: containerApps.outputs.environmentName
    containerRegistryName: containerApps.outputs.registryName
    accountingUri: accountingService.outputs.SERVICE_ACCOUNTING_URI
    virtualCustomersUri: virtualCustomer.outputs.SERVICE_VIRTUAL_CUSTOMERS_URI
    ordersUri: orderService.outputs.SERVICE_ORDER_URI
  }
  dependsOn: [
    accountingService
    virtualCustomer
    orderService
  ]
}

// module openAI './core/ai/openai.bicep' = {
//   name: '${deployment().name}--openai'
//   scope: rg
//   params: {
//     openAIServiceName: !empty(openAIAccountName) ? openAIAccountName : '${abbrs.openAIAccountName}${resourceToken}'
//     location: location
//   }
// }

// Data outputs
output AZURE_COSMOS_CONNECTION_STRING_KEY string = cosmos.outputs.connectionStringKey
output AZURE_COSMOS_DATABASE_NAME string = cosmos.outputs.databaseName
output AZURE_COSMOS_ACCOUNT_NAME string = cosmos.outputs.accountName

// App outputs
output APPLICATIONINSIGHTS_CONNECTION_STRING string = monitoring.outputs.applicationInsightsConnectionString
output APPLICATIONINSIGHTS_NAME string = monitoring.outputs.applicationInsightsName
output AZURE_CONTAINER_ENVIRONMENT_NAME string = containerApps.outputs.environmentName
output AZURE_CONTAINER_REGISTRY_ENDPOINT string = containerApps.outputs.registryLoginServer
output AZURE_CONTAINER_REGISTRY_NAME string = containerApps.outputs.registryName
output AZURE_LOCATION string = location
output AZURE_TENANT_ID string = tenant().tenantId
output REACT_APP_APPLICATIONINSIGHTS_CONNECTION_STRING string = monitoring.outputs.applicationInsightsConnectionString

output SERVICEBUS_NAMESPACE_NAME string = serviceBus.outputs.sbName
output SERVICE_ACCOUNTING_NAME string = accountingService.outputs.SERVICE_ACCOUNTING_NAME
output SERVICE_LOYALTY_NAME string = loyaltyService.outputs.SERVICE_LOYALTY_NAME
output SERVICE_MAKELINE_NAME string = makeLineService.outputs.SERVICE_MAKELINE_NAME
output SERVICE_ORDER_NAME string = orderService.outputs.SERVICE_ORDER_NAME
output SERVICE_RECEIPT_GENERATION_NAME string = receiptGenerationService.outputs.SERVICE_RECEIPT_GENERATION_NAME
output SERVICE_VIRTUAL_WORKER_NAME string = virtualWorker.outputs.SERVICE_VIRTUAL_WORKER_NAME
output SERVICE_VIRTUAL_CUSTOMERS_NAME string = virtualCustomer.outputs.SERVICE_VIRTUAL_CUSTOMERS_NAME
output SERVICE_UI_NAME string = ui.outputs.SERVICE_UI_NAME
