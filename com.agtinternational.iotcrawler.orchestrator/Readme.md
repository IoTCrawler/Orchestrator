# Orchestrator

The orchestrator component is responsible for interactions with client IoT applications. It allows applications to subscribe to streams without having a public endpoint as well as tracks subscription requests. In case of stream failure, orchestrator is able to notify application and provide a list of alternative streams for subscription. 

## Requirements

* OpenJDK 1.8
* Maven
* Docker

## Dependencies

* [Core](https://github.com/IoTCrawler/Orchestrator/tree/master/com.agtinternational.iotcrawler.core)

## Build

Use the following commands to build orchestrator and push image to the remote repo: 

```
sh make.sh install
sh make.sh build-image
sh make.sh push-image
```


## Configuration

Open [docker-compose.yml](com.agtinternational.iotcrawler.orchestrator/docker-compose.yml) and check/change the following variables:
* `HTTP_SERVER_PORT` - port to lister NGSI-LD commands
* `HTTP_REFERENCE_URL` - URL used as reference when subscription to MDR is ininitalized 
* `NGSILD_BROKER_URL` - URL of NGSI-LD endpoint of MDR
* `RANKING_COMPONENT_URL` - URL of NGSI-LD endpoint of [Ranking Component](https://github.com/IoTCrawler/Ranking)
* `IOTCRAWLER_REDIS_HOST` - Redis service endpoint (for persistence - optional)

## Deployment   
* Deployed online: https://orchestrator.iotcrawler.eu/ngsi-ld/v1/
* Works on top of broker: https://mdr.iotcrawler.eu