# Orchestrator

IoTCrawler Orchestrator component. Uses the following services:


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
