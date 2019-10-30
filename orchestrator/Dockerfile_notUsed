FROM java

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

ADD target /usr/src/app
ADD config /usr/src/app/config

#RUN mvn package -Dmaven.test.skip=true

CMD ["java", "-cp", "orchestrator-1.0.7.jar", "com.agtinternational.iotcrawler.orchestrator.Orchestrator"]
