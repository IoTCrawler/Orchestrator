#!/usr/bin/env bash
export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
echo ${CI_PROJECT_DIR}

if [ "$1" = "install" ]; then
	export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
	cd ${CI_PROJECT_DIR}/fiware && sh make.sh install
	cd ${CI_PROJECT_DIR}/core && mvn install -DskipTests=true
	cd ${CI_PROJECT_DIR}/orchestrator && make package
fi