# Orchestrator

The orchestrator component is responsible for interactions with client IoT applications. It allows applications to subscribe to streams without having a public endpoint as well as tracks subscription requests. In case of stream failure, orchestrator is able to notify application and provide a list of alternative streams for subscription. 

## Dependencies

See docker-compose.yml:
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

## Deployment   
* Deployed online: http://orchestrator-production.35.241.228.250.nip.io/ngsi-ld/v1/
* Works on top of broker: http://155.54.95.248:9090/ngsi-ld/v1
