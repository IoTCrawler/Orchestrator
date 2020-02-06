install:
	cd ~ &&  [ ! -d "fiware-ngsi2-api" ] && git clone git@gitlab.iotcrawler.net:orchestrator/fiware-ngsi2-api.git && cd ~/fiware-ngsi2-api/ngsi2-client && mvn install -DskipTests=true &
	mvn validate && mvn install -DskipTests=true