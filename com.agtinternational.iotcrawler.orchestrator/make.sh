#!/usr/bin/env bash

export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
echo ${CI_PROJECT_DIR}
set -e


if [ "$1" = "install" ]; then
	export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
  echo "# Orchestrator: Installing IoTCrawler fiware dependencies"
	#(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/fiware ]; then cd ${CI_PROJECT_DIR}/fiware && sh make.sh install; fi);
	cd ${CI_PROJECT_DIR}/../com.agtinternational.iotcrawler.fiware-clients && sh make.sh install
	echo "# Orchestrator: Installing IoTCrawler core dependency"
	#(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/core ]; then cd ${CI_PROJECT_DIR}/core && mvn install -DskipTests=true; fi);
	cd ${CI_PROJECT_DIR}/com.agtinternational.iotcrawler.core && sh make.sh install
	echo "# Orchestrator: Packaging orchestrator"
	cd ${CI_PROJECT_DIR} && make package
fi


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

if [ "$1" = "build-image" ]; then
  mvn clean compile jib:dockerBuild -U
fi

if [ "$1" = "push-image" ]; then
#  echo "# Setting env vars for pushing"
  if [ -z "$CI_COMMIT_TAG" ]; then
        export CI_APPLICATION_REPOSITORY=${CI_APPLICATION_REPOSITORY:-$CI_REGISTRY_IMAGE/$CI_COMMIT_REF_SLUG}
        export CI_APPLICATION_TAG=${CI_APPLICATION_TAG:-$CI_COMMIT_SHA}
      else
        export CI_APPLICATION_REPOSITORY=${CI_APPLICATION_REPOSITORY:-$CI_REGISTRY_IMAGE}
        export CI_APPLICATION_TAG=${CI_APPLICATION_TAG:-$CI_COMMIT_TAG}
  fi
#  if [[ -n "$CI_REGISTRY" && -n "$CI_REGISTRY_USER" ]]; then
#    echo "Logging to GitLab Container Registry with CI credentials..."
#    docker login -u "$CI_REGISTRY_USER" -p "$CI_REGISTRY_PASSWORD" "$CI_REGISTRY"
#  fi
# gitlab.iotcrawler.net:4567/orchestrator/orchestrator is already in variables (on in a gitlab)
  echo "# docker tag ${CI_APPLICATION_REPOSITORY}:${CI_APPLICATION_TAG} ${CI_APPLICATION_TAG}"
  docker tag ${CI_APPLICATION_REPOSITORY}:${CI_APPLICATION_TAG} ${CI_APPLICATION_TAG}
  echo "# docker push ${CI_APPLICATION_REPOSITORY}:$CI_APPLICATION_TAG"
  docker push ${CI_APPLICATION_REPOSITORY}:$CI_APPLICATION_TAG
	#docker push "${CI_APPLICATION_REPOSITORY}:latest"
fi