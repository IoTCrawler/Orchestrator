#!/usr/bin/env bash
#__dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
#
if [ "$1" = "prepare-djane" ]; then
	rm -rf ~/djane #not works on runner
	git clone https://github.com/sensinov/djane.git ~/djane
	sed -i "s~if (attributename == '@context')~if (attributename == '@context11')~g" ~/djane/models/entityModel.js
	sed -i "s~let authentication=true;~let authentication=false;~g" ~/djane/config/config.js
	#sed -i "s~build: .~image: gitlab.iotcrawler.net:4567/core/djane:1.0.0~g" ~/djane/docker-compose.yml
	docker-compose -f ~/djane/docker-compose.yml build
    #cd ~/djane && docker build . -t gitlab.iotcrawler.net:4567/core/djane:1.0.0
fi

if [ "$1" = "prepare-iotbroker" ]; then
	#Fiware/clients: Preparing iot-broker
	(if [ ! -d /tmp/iot.Aeron ]; then git clone https://gitlab+deploy-token-8:GcnYwtYSFLcAVsyKoUEZ@gitlab.iotcrawler.net/orchestrator/iot.Aeron.git /tmp/iot.Aeron ; fi);
	cd /tmp/iot.Aeron/IoTbrokerParent && mvn install -DskipTests=true
	cd /tmp/iot.Aeron/eu.neclab.iotplatform.ngsi.api && mvn install -DskipTests=true
	cd /tmp/iot.Aeron/eu.neclab.iotplatform.iotbroker.commons && mvn install -DskipTests=true
	cd /tmp/iot.Aeron/eu.neclab.iotplatform.iotbroker.client && mvn install -DskipTests=true
fi

if [ "$1" = "install" ]; then
		#Fiware/clients: Checking iot-broker dependency
	(if [ ! -d ~/.m2/repository/eu/neclab/iotplatform/iotbroker.client ]; then sh make.sh prepare-iotbroker; fi);
	#Fiware/clients: Checking fiware-models dependency
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/fiware-models ]; then cd ../com.agtinternational.iotcrawler.fiware-models && sh make.sh install; fi);
	#Fiware/clients: installing
	mvn install -DskipTests=true
fi
#
#if [ "$1" = "compose-up" ]; then
#    #sh make.sh prepare-djane
#    docker network create djanenet &
#    echo "Pulling needed images"
#    docker-compose -f docker-compose.yml pull
#    echo "Removing all running containers"
#    docker rm $(docker ps | awk '{print $1}') --force
#    echo "Starting docker-compose"
#    docker-compose -f docker-compose.yml up -d
#    echo "Sleeping 15s to start services" && sleep 15
#fi
#
#if [ "$1" = "test-ngsi-ld-client" ]; then
#	mvn -Dtest=com.agtinternational.iotcrawler.fiware.clients.NgsiLDClientTest test
#fi
#
#if [ "$1" = "test-iot-broker-client" ]; then
#	mvn -Dtest=com.agtinternational.iotcrawler.fiware.clients.IoTBrokerClientTest test
#fi
#
#if [ "$1" = "compose-down" ]; then
#    docker-compose -f docker-compose.yml down
#fi