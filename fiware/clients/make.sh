if [ "$1" == "prepare-djane" ]; then
	rm -rf ~/djane
	cd ~ && git clone https://github.com/sensinov/djane.git
	sed -i "s~if (attributename == '@context')~if (attributename == '@context11')~g" ~/djane/models/entityModel.js
	sed -i "s~let authentication=true;~let authentication=false;~g" ~/djane/config/config.js
	cd ~/djane && docker build . -t iotcrawler/djane:1.0.0
fi

if [ "$1" == "install" ]; then
	mvn validate && mvn install -DskipTests=true
fi

if [ "$1" == "test-iot-broker-client" ]; then
	docker run -d -t -p 8065:8065 -p 8060:8060 fiware/iotbroker:standalone-dev -p iotbroker_historicalagent="enabled"
	mvn test IoTBrokerClientTest#updateContextElement
	mvn test IoTBrokerClientTest#queryContextElementTest
fi

if [ "$1" == "test-ngsi-ld-client" ]; then
	docker run -d -p 3000:3000 iotcrawler/djane:1.0.0
    echo "Sleeping 15s" && sleep 15
	mvn -Dtest=NgsiLDClientTest surefire:test
	#mvn -Dtest=NgsiLDClientTest#addEntityTest surefire:test
	#mvn -Dtest=NgsiLDClientTest#updateEntityTest surefire:test
	#mvn -Dtest=NgsiLDClientTest#getEntitiesTest surefire:test
	#CID=$(docker ps | grep djane | awk '{print $1}') && echo "Stopping $CID" && docker stop $CID &
fi
