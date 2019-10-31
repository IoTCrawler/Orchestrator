#!/usr/bin/env bash
if [ "$1" = "install" ]; then
	cd fiware && sh install.sh && cd ../
	cd core && mvn install -DskipTests=true	
	#cd orchestrator && sh make.sh package
fi


if [ "$1" = "install-reqs" ]; then
	cd fiware/clients && sh make.sh prepare-djane
	cd orchestrator && sh make.sh install-reqs
fi

if [ "$1" = "start" ]; then
	cd orchestrator && docker-compose up &
	sleep 5
	cd orchestrator && docker-compose up orchestrator
	#cd orchestrator && sh iotbroker.sh &
	#cd orchestrator && make start &
fi	

if [ "$1" = "test-orchestrator" ]; then
	cd orchestrator && sh make.sh test
fi


if [ "$1" = "test-fiware-clients" ]; then
   	cd fiware/clients && sh make.sh test-ngsi-ld-client
fi 