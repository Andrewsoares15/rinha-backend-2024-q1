FROM ubuntu:24.04

MAINTAINER Andrew Soares

COPY build/native/nativeCompile/rinha /server

RUN apt-get update && apt-get install -y zlib1g

EXPOSE	8000

CMD ["/server","-Xms64m","-Xmx64m","-ea","-server"]