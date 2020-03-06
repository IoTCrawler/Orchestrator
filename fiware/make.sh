#!/usr/bin/env bash
export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
echo ${CI_PROJECT_DIR}
set -e

if [ "$1" = "install" ]; then
	# Fiware: Installing fiware-models
	cd $CI_PROJECT_DIR/fiware/models && make install
	# Fiware: Installing fiware-clients
	cd $CI_PROJECT_DIR/fiware/clients && make install
fi