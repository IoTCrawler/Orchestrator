# Orchestrator

IoTCrawler Orchestrator component. Uses the following services:

* RabbitMQ - to communicate with remote apps behind firewalls
* Redis - for persistently storing state information (active subscriptions)
* NGSI-LD broker - emulation of MDR


## Build

Build orchestrator and all supplementatry libraries

```
sh make.sh install
sh make.sh build-image
sh make.sh push-image
```


## Configuration

Open docker-compose.yml and check/change the following:

1) Orchestrator image URI - adjust the image name according to the latest one (outout)  
