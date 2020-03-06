#!/usr/bin/env bash
export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
echo ${CI_PROJECT_DIR}
set -e

if [ "$1" = "install" ]; then
	export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
  echo "# Main: Installing IoTCrawler fiware dependencies"
	#(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/fiware ]; then cd ${CI_PROJECT_DIR}/fiware && sh make.sh install; fi);
	cd ${CI_PROJECT_DIR}/fiware && sh make.sh install
	echo "# Main: Installing IoTCrawler core dependency"
	#(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/core ]; then cd ${CI_PROJECT_DIR}/core && mvn install -DskipTests=true; fi);
	cd ${CI_PROJECT_DIR}/core && mvn install -DskipTests=true
	echo "# Main: Packaging orchestrator"
	cd ${CI_PROJECT_DIR}/orchestrator && make package
fi