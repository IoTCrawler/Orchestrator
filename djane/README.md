# Modified djane 

A fork of the [https://github.com/sensinov/djane](https://github.com/sensinov/djane) where the [router.get('/entities'...](https://gitlab.iotcrawler.net/orchestrator/orchestrator/blob/master/djane/routes/entities.js) was modified to support:
* offset/limit
* filtering by attribute value

# Supported queries 
* http://djane:3000/ngsi-ld/v1/entities?type=Vehicle&offset=0&limit=1
* http://djane:3000/ngsi-ld/v1/entities?brandName.value=Mercedes&type=Vehicle

#Image

gitlab.iotcrawler.net:4567/core/djane:1.0.0