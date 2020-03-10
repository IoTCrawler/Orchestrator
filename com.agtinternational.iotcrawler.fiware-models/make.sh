#!/usr/bin/env bash
set -e

if [ "$1" = "prepare-ngsi2-api" ]; then
	(if [ ! -d /tmp/fiware-ngsi2-api ]; then git clone https://gitlab+deploy-token-8:GcnYwtYSFLcAVsyKoUEZ@gitlab.iotcrawler.net/orchestrator/fiware-ngsi2-api.git /tmp/fiware-ngsi2-api; fi);
	cd /tmp/fiware-ngsi2-api && mvn install -DskipTests=true
	cd /tmp/fiware-ngsi2-api/ngsi2-client && mvn install -DskipTests=true
fi

if [ "$1" = "install" ]; then
    #Fiware/models: checking ngsi2 dependency
	(if [ ! -d ~/.m2/repository/com/orange/fiware/ngsi2-client ] || [ -n "$REBUILD_ALL" ]; then sh make.sh prepare-ngsi2-api; fi);

  # Fiware-models: checking com/agtinternational/iotcrawler
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler ] || [ -n "$REBUILD_ALL" ]; then cd "${CI_PROJECT_DIR}/IoTCrawler" && mvn install; fi);
	#Fiware/models: installing
	mvn validate && mvn install -DskipTests=true
fi
