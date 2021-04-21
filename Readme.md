## Contents

The repo contains hierarchy of maven packages depending on each other:
* [Fiware-Models](com.agtinternational.iotcrawler.fiware-models) - EntityLD model (lightweight)
* [Core](com.agtinternational.iotcrawler.core) - models of core IoTCrawler ontology (lightweight)
* [Fiware-Clients](com.agtinternational.iotcrawler.fiware-clients) - NGSI-LD client based on NGSI-Libraries
* [Orchestrator](com.agtinternational.iotcrawler.orchestrator) - Orchestrator component

## Requirements

* OpenJDK 1.8
* Maven
* Docker


## Build & install (all)

Build all the packages and install them in local maven repo:

```
sh make.sh install
```

Components can be built and installed individially from the corresponding folders. 
