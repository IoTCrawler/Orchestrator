#!/usr/bin/env bash
set -e

if [ "$1" = "prepare-ngsi2-api" ]; then
	(if [ ! -d /tmp/fiware-ngsi2-api ] ; then rm -rf /tmp/fiware-ngsi2-api && git clone https://gitlab+deploy-token-8:GcnYwtYSFLcAVsyKoUEZ@gitlab.iotcrawler.net/orchestrator/fiware-ngsi2-api.git /tmp/fiware-ngsi2-api; fi);
	CURR=$(pwd) && cd /tmp/fiware-ngsi2-api && mvn install -DskipTests=true && cd $CURR
	CURR=$(pwd) && cd /tmp/fiware-ngsi2-api/ngsi2-client && mvn install -DskipTests=true && cd $CURR
fi

if [ "$1" = "install" ]; then
   echo "#Fiware/models: checking ngsi2 dependency"
	(if [ ! -d ~/.m2/repository/com/orange/fiware/ngsi2-client ]; then CURR=$(pwd) && sh make.sh prepare-ngsi2-api && cd $CURR; fi);

  echo "# Fiware-models: checking com/agtinternational/iotcrawler"
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler ]; then CURR=$(pwd) && cd "../IoTCrawler" && mvn install && cd $CURR; fi);
	echo "#Fiware/models: installing"
	mvn validate && mvn clean install -DskipTests=true
fi
