build-agent:
	docker build -t gitlab.iotcrawler.net:4567/core/maven-docker-compose .

push-agent:
	docker build -t gitlab.iotcrawler.net:4567/core/maven-docker-compose .

# install-reqs:
# 	cd $CI_PROJECT_DIR/orchestrator && make install-reqs

test-orchestrator:
	cd $CI_PROJECT_DIR/orchestrator && make test

test-fiware-clients:
	cd $CI_PROJECT_DIR/fiware/clients && make test
