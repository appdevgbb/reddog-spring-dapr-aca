param name string
param location string = resourceGroup().location
param tags object = {}


resource redis 'Microsoft.Cache/redis@2022-06-01' = {
  name: name
  location: location
  tags: tags
  properties:{
    sku: {
      name: 'Standard'
      family: 'C'
      capacity: 1
    }
  }
}

output redisHost string = redis.properties.hostName
output redisPassword string = listKeys(redis.id, redis.apiVersion).primaryKey
output redisName string = name
