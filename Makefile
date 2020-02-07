build-agent:
	docker build -t gitlab.iotcrawler.net:4567/core/maven-docker-compose .

push-agent:
	docker build -t gitlab.iotcrawler.net:4567/core/maven-docker-compose .

install:
	cd fiware && make install && cd ..
	cd core && mvn install -DskipTests=true	 && cd ..
	cd orchestrator && make package && cd ..

install-reqs:
	cd orchestrator && make install-reqs


start:
	cd orchestrator && docker-compose up &
	sleep 5
	cd orchestrator && docker-compose up orchestrator
	#cd orchestrator && sh iotbroker.sh &
	#cd orchestrator && make start &
	exi
test-orchestrator:
	cd orchestrator && make test

test-fiware-clients:
	cd fiware/clients && make test
