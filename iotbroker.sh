CID=$(docker ps -a | grep iotbroker | awk '{print $1}' | head -n 1)
if [ "$CID" != "" ]; 
  then
    echo "Starting $CID"
	docker start $CID &
  else
    echo "Running a new container"
    docker run -d -t -p 8065:8065 -p 8060:8060 fiware/iotbroker:standalone-dev -p iotbroker_historicalagent="enabled" &
fi