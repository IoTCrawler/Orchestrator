#!/usr/bin/env bash
export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
echo "CI_PROJECT_DIR: ${CI_PROJECT_DIR}"
set -e


if [ "$1" = "install" ]; then
	export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
  echo "# Main: checking com/agtinternational/iotcrawler"
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler ]; then cd "${CI_PROJECT_DIR}/IoTCrawler" && mvn install; fi);
	echo "# Main: checking com/agtinternational/iotcrawler/fiware-models"
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/fiware-models ]; then cd "${CI_PROJECT_DIR}/com.agtinternational.iotcrawler.fiware-models" && sh make.sh install; fi);
	echo "# Main: checking com/agtinternational/iotcrawler/fiware-clients"
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/fiware-clients ]; then cd "${CI_PROJECT_DIR}/com.agtinternational.iotcrawler.fiware-clients" && sh make.sh install; fi);
  echo "# Main: checking com/agtinternational/iotcrawler/core"
	(if [ ! -d ~/.m2/repository/com/agtinternational/iotcrawler/core ]; then cd "${CI_PROJECT_DIR}/com.agtinternational.iotcrawler.core" && mvn install -DskipTests=true; fi);
fi