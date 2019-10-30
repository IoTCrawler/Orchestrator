cd fiware-ngsi2-api_patched && mvn install -DskipTests=true && cd ../
cd models && mvn install -DskipTests=true && cd ../
cd clients && mvn validate && mvn install -DskipTests=true && cd ../
