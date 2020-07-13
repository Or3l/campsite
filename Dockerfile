FROM maven:3.6.3-jdk-8-slim

ENV APP_FOLDER=/opt/campsite
ENV SPRING_PROFILE_ENV=""
ENV JAR_NAME=campsite-0.0.1-SNAPSHOT.jar

WORKDIR ${APP_FOLDER}

COPY ./src ${APP_FOLDER}/src
COPY pom.xml ${APP_FOLDER}
RUN mkdir ${APP_FOLDER}/target/
RUN mvn clean package spring-boot:repackage

VOLUME /tmp

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java -jar ${APP_FOLDER}/target/${JAR_NAME}" ]