package:
	mvn package -DskipTests=true
	
build-image:
	mvn compile jib:dockerBuild
	docker push gitlab.iotcrawler.net:4567/orchestrator/orchestrator:1.0.9

export-image:
	docker save iotcrawler/orchestrator > orchestator.tar

import-image:
	docker load < orchestrator.tar

install-reqs:
	docker network create iotcrawler &	

start-containers:
	make install-reqs
	echo "Pulling needed images"
	docker-compose pull
	echo "Stopping any already running containers"
	docker ps | grep "core/djane" | cut -c1-12 | xargs docker rm --force &
	docker ps | grep "fiware/iotbroker" | cut -c1-12 | xargs docker rm --force &
	docker ps | grep mongo | cut -c1-12 | xargs docker rm --force &
	docker ps | grep orchestrator | cut -c1-12 | xargs docker rm --force &
	sleep 5
	echo "Starting docker-compose"
	docker-compose -f docker-compose.yml up -d
	echo "Containers should be started"
start:
	#docker-compose up &
	#export HTTP_SERVER_HOST=192.168.56.101 && java -cp target/orchestrator-1.0.7.jar com.agtinternational.iotcrawler.orchestrator.Orchestrator
	export HTTP_SERVER_HOST=10.59.0.101 && export IOTCRAWLER_RABBIT_HOST=rabbit && export IOTCRAWLER_REDIS_HOST=redis && export NGSILD_BROKER_URI=http://djane:3000/ngsi-ld/ && export IOT_BROKER_URI=http://iotbroker:8060/ngsi10  && java -jar target/orchestrator-1.0.8.jar
	
orchestrator-test:
	mvn -Dtest=OrchestratorTest test
	#mvn -Dtest=OrchestratorTest surefire:test

rpc-client-test:
	mvn -Dtest=RPCClientTest test
	#mvn -Dtest=RPCClientTest surefire:test

stop-containers:
	echo "Stopping running containers"	
	docker-compose down
