FROM ubuntu:latest

RUN apt-get update && apt-get install software-properties-common curl libltdl7 make -y 
RUN add-apt-repository ppa:openjdk-r/ppa && apt-get update && apt-get install openjdk-8-jdk maven -y

RUN curl -L "https://github.com/docker/compose/releases/download/1.23.1/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose
