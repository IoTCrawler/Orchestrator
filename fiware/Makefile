install:
	cd fiware-ngsi2-api_patched && mvn -DskipTests=true -Dmaven.clean.failOnError=false install
	cd models && mvn -DskipTests=true -Dmaven.clean.failOnError=false install
	cd clients && make install
