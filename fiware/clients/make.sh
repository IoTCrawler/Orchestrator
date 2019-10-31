__dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [ "$1" = "prepare-djane" ]; then
	sudo rm -rf ~/djane && sleep 2
	ls ~/djane
	git clone https://github.com/sensinov/djane.git ~/djane && sleep 2
	cat ~/djane/docker-compose.yml
	#sed -i "s~if (attributename == '@context')~if (attributename == '@context11')~g" ~/djane/models/entityModel.js
	sed -i "s~let authentication=true;~let authentication=false;~g" ~/djane/config/config.js
	#sed -i "s~build: .~image: gitlab.iotcrawler.net:4567/core/djane:1.0.0~g" ~/djane/docker-compose.yml
	#docker-compose -f ~/djane/docker-compose.yml build
    
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
    sh make.sh prepare-djane
    #docker network create djanenet &
    docker rm $(docker ps | awk '{print $1}') --force
    #docker-compose -f ~/djane/docker-compose.yml up
    #echo "Sleeping 15s before starting tests" && sleep 15
	#mvn -Dtest=NgsiLDClientTest test
	#mvn -Dtest=NgsiLDClientTest#addEntityTest surefire:test
	#mvn -Dtest=NgsiLDClientTest#updateEntityTest surefire:test
	#mvn -Dtest=NgsiLDClientTest#getEntitiesTest surefire:test
    #docker-compose -f ~/djane/docker-compose.yml down
	#echo "Stopping djane $CID" && docker stop $CID &
    #echo "Stopping mongo $MID" && docker stop $MID &
fi 

