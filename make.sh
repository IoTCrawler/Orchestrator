#!/usr/bin/env bash
#export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
#echo "CI_PROJECT_DIR: ${CI_PROJECT_DIR}"
set -e


if [ "$1" = "install" ]; then

  (if [ -n "$REBUILD_ALL" ]; then echo "rm -rf ~/.m2/repository/com/agtinternational/iotcrawler" && rm -rf ~/.m2/repository/com/agtinternational/iotcrawler; fi);

  echo "# Main: checking com/agtinternational/iotcrawler"
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler ]; then cd "IoTCrawler" && mvn install || cd ..; fi);
	echo "# Main: checking com/agtinternational/iotcrawler/fiware-models"
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/fiware-models ]; then cd "com.agtinternational.iotcrawler.fiware-models" && sh make.sh install || cd ..; fi);
	echo "# Main: checking com/agtinternational/iotcrawler/fiware-clients"
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/fiware-clients ]; then cd "com.agtinternational.iotcrawler.fiware-clients" && sh make.sh install || cd ..; fi);
  echo "# Main: checking com/agtinternational/iotcrawler/core"
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/core ]; then cd "com.agtinternational.iotcrawler.core" && mvn install -DskipTests=true || cd ..; fi);
	cd "com.agtinternational.iotcrawler.orchestrator" && sh make.sh install || cd ..
fi

if [ "$1" = "build-image" ]; then
  cd "com.agtinternational.iotcrawler.orchestrator" && sh make.sh build-image || cd ..
fi

if [ "$1" = "push-image" ]; then
  cd "com.agtinternational.iotcrawler.orchestrator" && sh make.sh push-image || cd ..
fi