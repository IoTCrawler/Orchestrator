install:
	(if [ ! -d /tmp/fiware-ngsi2-api ]; then git clone https://gitlab+deploy-token-8:GcnYwtYSFLcAVsyKoUEZ@gitlab.iotcrawler.net/orchestrator/fiware-ngsi2-api.git /tmp/fiware-ngsi2-api; fi);
	(if [ ! -d ~/.m2/repository/com/orange/fiware/ngsi2-client ]; then cd /tmp/fiware-ngsi2-api && mvn install -DskipTests=true && cd /tmp/fiware-ngsi2-api/ngsi2-client && mvn install -DskipTests=true; fi);
	mvn validate && mvn install -DskipTests=true