package:
	mvn package -DskipTests=true
	
build-image:
	mvn compile jib:dockerBuild

export-image:
	docker save iotcrawler/orchestrator > orchestator.tar

import-image:
	docker load < orchestrator.tar

install-reqs:
	docker network create iotcrawler &	

start:
	#docker-compose up &
	#export HTTP_SERVER_HOST=192.168.56.101 && java -cp target/orchestrator-1.0.7.jar com.agtinternational.iotcrawler.orchestrator.Orchestrator
	export HTTP_SERVER_HOST=10.59.0.101 && export NGSILD_BROKER_URI=http://localhost:3000/ngsi-ld/ && export IOT_BROKER_URI=http://localhost:8060/ngsi10  && java -jar target/orchestrator-1.0.7.jar
	
test:
	mvn -Dtest=OrchestratorTest#orchrestratorTest surefire:test
