install:
	cd fiware && make install
	cd core && mvn install -DskipTests=true	
	cd orchestrator && make package

install-reqs:
	cd fiware/clients && make prepare-djane
	cd orchestrator && make install-reqs
start:
	cd orchestrator && docker-compose up &
	cd orchestrator && sh iotbroker.sh &
	cd orchestrator && make start &
	
test-orchestrator:
	cd orchestrator && make test