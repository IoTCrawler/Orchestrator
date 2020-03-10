prepare-djane:
	rm -rf ~/djane
	cd ~ && git clone https://github.com/sensinov/djane.git
	sed -i "s~if (attributename == '@context')~if (attributename == '@context11')~g" ~/djane/models/entityModel.js
	sed -i "s~let authentication=true;~let authentication=false;~g" ~/djane/config/config.js
	cd ~/djane && docker build . -t gitlab.iotcrawler.net:4567/core/djane:1.0.0


prepare-scorpio:
	rm -rf ~/scorpio
	cd ~ && git clone https://github.com/ScorpioBroker/ScorpioBroker.git
	#sed -i "s~if (attributename == '@context')~if (attributename == '@context11')~g" ~/djane/models/entityModel.js
	#sed -i "s~let authentication=true;~let authentication=false;~g" ~/djane/config/config.js
	cd ~/djane && docker build . -t iotcrawler/djane:1.0.0

prepare-iotbroker:
	#Fiware/clients: Preparing iot-broker
	(if [ ! -d /tmp/iot.Aeron ] || [ -n "$REBUILD_ALL" ]; then rm -rf /tmp/iot.Aeron && git clone https://gitlab+deploy-token-10:jCz86jEA8FPs3wpjhoN-@gitlab.iotcrawler.net/orchestrator/iot.Aeron.git /tmp/iot.Aeron ; fi);
	cd /tmp/iot.Aeron/IoTbrokerParent && mvn install -DskipTests=true
	cd /tmp/iot.Aeron/eu.neclab.iotplatform.ngsi.api && mvn install -DskipTests=true
	cd /tmp/iot.Aeron/eu.neclab.iotplatform.iotbroker.commons && mvn install -DskipTests=true
	cd /tmp/iot.Aeron/eu.neclab.iotplatform.iotbroker.client && mvn install -DskipTests=true

install:
	#Fiware/clients: Checking iot-broker dependency
	(if [ ! -d ~/.m2/repository/eu/neclab/iotplatform/iotbroker.client ] || [ -n "$REBUILD_ALL" ]; then make prepare-iotbroker; fi);
	#Fiware/clients: Checking fiware-models dependency
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/fiware-models ] || [ -n "$REBUILD_ALL" ]; then cd ../fiware-models && make install; fi);
	#Fiware/clients: installing
	mvn install -DskipTests=true

# start-containers:
# 	make stop-containers
# 	docker network create djanenet &
# 	echo "Pulling needed images"
# 	docker-compose pull
# 	echo "Starting docker-compose"
# 	docker-compose -f docker-compose.yml up -d

ngsi-ld-client-test:
	mvn -Dtest=com.agtinternational.iotcrawler.fiware.clients.NgsiLDClientTest test
	#mvn -Dtest=com.agtinternational.iotcrawler.fiware.clients.NgsiLDClientTest#addEntityTest surefire:test
	#mvn -Dtest=com.agtinternational.iotcrawler.fiware.clients.NgsiLDClientTest#updateEntityTest surefire:test
	#mvn -Dtest=com.agtinternational.iotcrawler.fiware.clients.NgsiLDClientTest#getEntitiesTest surefire:test

iot-broker-client-test:
	mvn -Dtest=com.agtinternational.iotcrawler.fiware.clients.IoTBrokerClientTest test

stop-containers:
	echo "Stopping running containers"	
	docker-compose down
