#!/usr/bin/env bash
export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
echo ${CI_PROJECT_DIR}
set -e

if [ "$1" = "install" ]; then
	export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
  # Main: Checking IoTCrawler fiware dependencies
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/fiware ]; then cd ${CI_PROJECT_DIR}/fiware && sh make.sh install; fi);
	# Main: Checking IoTCrawler core dependency
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/core ]; then cd ${CI_PROJECT_DIR}/core && mvn install -DskipTests=true; fi);
	# Main: Packaging orchestrator
	cd ${CI_PROJECT_DIR}/orchestrator && make package
fi