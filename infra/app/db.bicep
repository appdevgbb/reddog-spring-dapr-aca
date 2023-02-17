param accountName string
param location string = resourceGroup().location
param tags object = {}

param loyaltyContainerName string = 'loyalty'
param productsContainerName string = 'products'
param ordersContainerName string = 'orders'


param containers array = [
  {
    name: loyaltyContainerName
    id: loyaltyContainerName
    partitionKey : '/partitionKey'
  }
  {
    name: productsContainerName
    id: productsContainerName
    partitionKey : '/partitionKey'
  }
  {
    name: ordersContainerName
    id: ordersContainerName
    partitionKey : '/partitionKey'
  }
]
param databaseName string = ''

// Because databaseName is optional in main.bicep, we make sure the database name is set here.
var defaultDatabaseName = 'Reddog'
var actualDatabaseName = !empty(databaseName) ? databaseName : defaultDatabaseName

module cosmos '../core/database/cosmos/sql/cosmos-sql-db.bicep' = {
  name: 'cosmos-sql'
  params: {
    accountName: accountName
    databaseName: actualDatabaseName
    location: location
    containers: containers
    tags: tags
  }
}

output connectionStringKey string = cosmos.outputs.connectionStringKey
output databaseName string = cosmos.outputs.databaseName
output endpoint string = cosmos.outputs.endpoint
output accountName string = cosmos.outputs.accountName
output loyaltyContainerName string = loyaltyContainerName
output productsContainerName string = productsContainerName
output ordersContainerName string = ordersContainerName
