## alpine Linux with JRE
FROM eclipse-temurin:21-jre-alpine

## Set environnement JAVA
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="$PATH:$JAVA_HOME/bin"

## create non root user and group
RUN addgroup -S spring && adduser -S spring -G spring

## copy projet
ARG WAR_FILE=target/sepa26server.jar
COPY ${WAR_FILE} /opt/sepa26server.war

## Set the nonroot user as default user
USER spring:spring

# choose working directory
WORKDIR /opt

ENTRYPOINT ["java", "-jar", "sepa26server.jar"]

## Expose the port
EXPOSE 8100
