package:
	mvn package -DskipTests=true
	
build-image:
	mvn compile jib:dockerBuild

push-image:
	docker push gitlab.iotcrawler.net:4567/orchestrator/orchestrator:1.0.9

export-image:
	docker save iotcrawler/orchestrator > orchestator.tar

import-image:
	docker load < orchestrator.tar

create-network:
	docker network create iotcrawler > /dev/null 2>&1 &

start-containers:
	make create-network
	echo "Pulling needed images"
	#docker-compose pull
	echo "Stopping any already running containers"
	docker ps -a | grep djane && docker ps -a | grep djane | cut -c1-12 | xargs docker rm --force &
	docker ps -a | grep iotbroker && docker ps -a | grep iotbroker | cut -c1-12 | xargs docker rm --force &
	docker ps -a | grep mongo && docker ps -a | grep mongo | cut -c1-12 | xargs docker rm --force &
	docker ps -a | grep orchestrator && docker ps -a | grep orchestrator | cut -c1-12 | xargs docker rm --force &
	sleep 5
	echo "Starting docker-compose"
	docker-compose -f docker-compose.yml up -d
	echo "Containers should be started"
	docker-compose ps
start:
	#docker-compose up &
	#export HTTP_SERVER_HOST=192.168.56.101 && java -cp target/orchestrator-1.0.7.jar com.agtinternational.iotcrawler.orchestrator.Orchestrator
	export HTTP_SERVER_HOST=10.59.0.101 && export IOTCRAWLER_RABBIT_HOST=rabbit && export IOTCRAWLER_REDIS_HOST=redis && export NGSILD_BROKER_URI=http://djane:3000/ngsi-ld/ && export IOT_BROKER_URI=http://iotbroker:8060/ngsi10  && java -jar target/orchestrator-1.0.8.jar
	
orchestrator-test:
	mvn -Dtest=OrchestratorTest test
	#mvn -Dtest=OrchestratorTest surefire:test

rpc-client-test:
	export JAVA_TOOL_OPTIONS="-Xmx12m -Xms12m"
	mvn -Dtest=RPCClientTest test
	#mvn -Dtest=RPCClientTest surefire:test

stop-containers:
	echo "Stopping running containers"	
	docker-compose down
