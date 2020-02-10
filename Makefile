build-agent:
	docker build -t gitlab.iotcrawler.net:4567/core/maven-docker-compose .

push-agent:
	docker build -t gitlab.iotcrawler.net:4567/core/maven-docker-compose .

install:
    CI_PROJECT_DIR=${CI_PROJECT_DIR:-$(pwd)}
	cd $CI_PROJECT_DIR/fiware && make install
	cd $CI_PROJECT_DIR/core && mvn install -DskipTests=true
	cd $CI_PROJECT_DIR/orchestrator && make package

install-reqs:
	cd $CI_PROJECT_DIR/orchestrator && make install-reqs

start:
	cd $CI_PROJECT_DIR/orchestrator && docker-compose up &
	sleep 5
	cd $CI_PROJECT_DIR/orchestrator && docker-compose up orchestrator
	#cd orchestrator && sh iotbroker.sh &
	#cd orchestrator && make start &

test-orchestrator:
	cd $CI_PROJECT_DIR/orchestrator && make test

test-fiware-clients:
	cd $CI_PROJECT_DIR/fiware/clients && make test
