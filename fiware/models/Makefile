install:
	[ ! -d ~/.m2/repository/com/orange/fiware/ngsi2-client ] && cd /tmp && rm -rf fiware-ngsi2-api && git clone git@gitlab.iotcrawler.net:orchestrator/fiware-ngsi2-api.git && cd fiware-ngsi2-api && mvn install -DskipTests=true && cd ngsi2-client && mvn install -DskipTests=true || echo "fiware-ngsi2-api is ready"
	mvn validate && mvn install -DskipTests=true