prepare-djane:
	rm -rf ~/djane
	cd ~ && git clone https://github.com/sensinov/djane.git
	sed -i "s~if (attributename == '@context')~if (attributename == '@context11')~g" ~/djane/models/entityModel.js
	sed -i "s~let authentication=true;~let authentication=false;~g" ~/djane/config/config.js
	cd ~/djane && docker build . -t iotcrawler/djane:1.0.0

install:
	mvn validate && mvn install -DskipTests=true

start-containers:
	make stop-containers
	docker network create djanenet &
	echo "Pulling needed images"
	docker-compose pull
	echo "Starting docker-compose"
	docker-compose -f docker-compose.yml up -d

ngsi-ld-client-test:
	mvn -Dtest=NgsiLDClientTest test
	#mvn -Dtest=NgsiLDClientTest#addEntityTest surefire:test
	#mvn -Dtest=NgsiLDClientTest#updateEntityTest surefire:test
	#mvn -Dtest=NgsiLDClientTest#getEntitiesTest surefire:test

iot-broker-client-test:
	mvn -Dtest=IoTBrokerClientTest test

stop-containers:
	echo "Stopping running containers"	
	docker-compose down
