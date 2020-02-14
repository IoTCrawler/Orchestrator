if [ "$1" = "rest-client-test" ]; then
  # Orchestrator: rest-client-test
	#export JAVA_TOOL_OPTIONS="-Xmx256m -Xms256m"
	mvn package -DskipTests=true
	echo "export ORCHESTRATOR_ID=$(docker ps | grep orchestrator/master | awk '{print $1}')"
	ORCHESTRATOR_ID=$(docker ps | grep "orchestrator/master" | awk '{print $1}')
	export ORCHESTRATOR_ID=$ORCHESTRATOR_ID
	docker inspect ${ORCHESTRATOR_ID}
	export ORCHESTRATOR_IP=$(docker inspect -f "{{ .NetworkSettings.Networks.orchestrator_default.IPAddress}}" ${ORCHESTRATOR_ID})
	export IOTCRAWLER_ORCHESTRATOR_URL=http://${ORCHESTRATOR_IP}:3001/ngsi-ld/
	echo "IOTCRAWLER_ORCHESTRATOR_URL=${IOTCRAWLER_ORCHESTRATOR_URL}"
	mvn -Dtest=OrchestratorTestsREST -DIOTCRAWLER_ORCHESTRATOR_URL=${IOTCRAWLER_ORCHESTRATOR_URL} surefire:test
fi

#if [ "$1" = "rpc-client-test" ]; then
#rpc-client-test:
#	export JAVA_TOOL_OPTIONS="-Xmx256m -Xms256m"
#	export IOTCRAWLER_RABBIT_HOST=localhost
#	export IOTCRAWLER_REDIS_HOST=localhost
#	export NGSILD_BROKER_URL=http://localhost:3000/ngsi-ld/
#	mvn package -DskipTests=true
#	mvn -Dtest=OrchestratorTestsRPC surefire:test
#fi