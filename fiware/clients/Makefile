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


install:
	(if [ ! -d /tmp/iot.Aeron ]; then git clone git@gitlab.iotcrawler.net:orchestrator/iot.Aeron.git /tmp/iot.Aeron ; fi);
	(if [ ! -d ~/.m2/repository/eu/neclab/iotplatform ]; then cd /tmp/iot.Aeron/IoTbrokerParent && mvn install -DskipTests=true && cd /tmp/iot.Aeron/eu.neclab.iotplatform.ngsi.api && mvn install -DskipTests=true && cd /tmp/iot.Aeron/eu.neclab.iotplatform.iotbroker.commons && mvn install -DskipTests=true && cd /tmp/iot.Aeron/eu.neclab.iotplatform.iotbroker.client && mvn install -DskipTests=true; fi);
	(if [ ! -d /tmp/fiware-ngsi2-api ]; then git clone https://gitlab+deploy-token-8:GcnYwtYSFLcAVsyKoUEZ@gitlab.iotcrawler.net/orchestrator/fiware-ngsi2-api.git /tmp/fiware-ngsi2-api; fi);
   	(if [ ! -d ~/.m2/repository/com/orange/fiware/ngsi2-client ]; then cd /tmp/fiware-ngsi2-api && mvn install -DskipTests=true && cd /tmp/fiware-ngsi2-api/ngsi2-client && mvn install -DskipTests=true; fi);
	mvn install -DskipTests=true

start-containers:
	make stop-containers
	docker network create djanenet &
	echo "Pulling needed images"
	docker-compose pull
	echo "Starting docker-compose"
	docker-compose -f docker-compose.yml up -d

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
