FROM docker:latest
FROM java:8
FROM maven:alpine

RUN apk add --no-cache py-pip python-dev libffi-dev openssl-dev gcc libc-dev make && \
    pip install docker-compose


