param name string
param location string = resourceGroup().location
param tags object = {}


resource redis 'Microsoft.Cache/redisEnterprise@2022-01-01' = {
  name: name
  location: location
  tags: tags
  sku: {
    capacity: 2
    name: 'Enterprise_E10'
  }
}

output redisHost string = redis.properties.hostName
output redisPassword string = listKeys(redis.id, redis.apiVersion).primaryKey
output redisName string = name
