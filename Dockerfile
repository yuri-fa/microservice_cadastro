FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/localsurf-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 80
MAINTAINER YURI