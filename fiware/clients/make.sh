__dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ "$1" = "prepare-djane" ]; then
	rm -rf ~/djane
	cd ~ && git clone https://github.com/sensinov/djane.git
	cd ~/djane
    ls
    sed -i "s~if (attributename == '@context')~if (attributename == '@context11')~g" models/entityModel.js
	sed -i "s~let authentication=true;~let authentication=false;~g" config/config.js
	sed -i "s~build: .~image: gitlab.iotcrawler.net:4567/core/djane:1.0.0~g" config/config.js
    #docker-compose build
    cd ..
	#cd ~/djane && docker build . -t gitlab.iotcrawler.net:4567/core/djane:1.0.0
fi

if [ "$1" = "install" ]; then
	mvn validate && mvn install -DskipTests=true
fi

if [ "$1" = "test-iot-broker-client" ]; then
	docker run -d -t -p 8065:8065 -p 8060:8060 fiware/iotbroker:standalone-dev -p iotbroker_historicalagent="enabled"
	mvn test IoTBrokerClientTest#updateContextElement
	mvn test IoTBrokerClientTest#queryContextElementTest
fi

if [ "$1" = "test-ngsi-ld-client" ]; then
    docker network create djanenet &
    cd ~/djane && docker-compose up -d
    echo "Sleeping 15s before starting tests" && sleep 15
	cd $__dir && mvn -Dtest=NgsiLDClientTest surefire:test
	#mvn -Dtest=NgsiLDClientTest#addEntityTest surefire:test
	#mvn -Dtest=NgsiLDClientTest#updateEntityTest surefire:test
	#mvn -Dtest=NgsiLDClientTest#getEntitiesTest surefire:test
    cd ~/djane && docker-compose down
	#echo "Stopping djane $CID" && docker stop $CID &
    #echo "Stopping mongo $MID" && docker stop $MID &
fi 

