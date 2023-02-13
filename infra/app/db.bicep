param accountName string
param location string = resourceGroup().location
param tags object = {}

param collectionName string = 'reddog'

param containers array = [
  {
    name: collectionName
    id: collectionName
    partitionKey : '/id'
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
output collectionName string = collectionName
