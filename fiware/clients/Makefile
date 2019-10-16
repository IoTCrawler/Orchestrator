prepare-djane:
	rm -rf ~/djane
	cd ~ && git clone https://github.com/sensinov/djane.git
	sed -i "s~if (attributename == '@context')~if (attributename == '@context11')~g" ~/djane/models/entityModel.js
	sed -i "s~let authentication=true;~let authentication=false;~g" ~/djane/config/config.js
	cd ~/djane && docker build . -t iotcrawler/djane:1.0.0

install:
	mvn validate && mvn install -DskipTests=true

test-iot-broker-client:
	docker run -d -t -p 8065:8065 -p 8060:8060 fiware/iotbroker:standalone-dev -p iotbroker_historicalagent="enabled"
	mvn test IoTBrokerClientTest#updateContextElement
	mvn test IoTBrokerClientTest#queryContextElementTest

test-ngsi-ld-client:
	docker run -d --network host -p 3000:3000 iotcrawler/djane:1.0.0
	mvn test NgsiLDClientTest#addEntityTest
	mvn test NgsiLDClientTest#updateEntityTest
	mvn test NgsiLDClientTest#getEntitiesTest