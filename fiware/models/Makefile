install:
	[ ! -d /tmp/fiware-ngsi2-api ] && cd /tmp && git clone git@gitlab.iotcrawler.net:orchestrator/fiware-ngsi2-api.git
	[ ! -d ~/.m2/repository/com/orange/fiware/ngsi2-client ] && cd /tmp/fiware-ngsi2-api && mvn install -DskipTests=true && cd /tmp/fiware-ngsi2-api/ngsi2-client && mvn install -DskipTests=true || echo "fiware-ngsi2-api is ready"
	mvn validate && mvn install -DskipTests=true