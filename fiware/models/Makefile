install:
	cd /tmp &&  [ ! -d "fiware-ngsi2-api" ] && git clone git@gitlab.iotcrawler.net:orchestrator/fiware-ngsi2-api.git && cd /tmp/fiware-ngsi2-api && mvn install -DskipTests=true || echo "fiware-ngsi2-api is ready"
	mvn validate && mvn install -DskipTests=true