#!/usr/bin/env bash
export CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
echo ${CI_PROJECT_DIR}

if [ "$1" = "install" ]; then
	cd $CI_PROJECT_DIR/fiware/models && make install
	cd $CI_PROJECT_DIR/fiware/clients && make install
fi