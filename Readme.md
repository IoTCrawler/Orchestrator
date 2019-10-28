# Orchestrator

IoTCrawler Orchestrator component. Uses the following services:

RabbitMQ - to communicate with remote apps behind firewalls
Redis - for persistently storing state information (active subscriptions)
NGSI-LD broker - emulation of MDR

## Build

Build orchestrator and all supplementatry libraries

```shell
make install
```

---

## Run

Prepare for running (prepare docker networks & djane image)

```shell
make install-reqs
```


Run ngsi-ld broker, rabbitMQ, redis

```shell
make start
```
