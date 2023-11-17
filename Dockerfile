# Use an official OpenJDK runtime as a parent image
#FROM openjdk:8-jdk-alpine
#WORKDIR /app
#COPY ./target/meetagora-0.0.1-SNAPSHOT.jar meetagora-app.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "meetagora-0.0.1-SNAPSHOT.jar"]
FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY ./target/meetagora-0.0.1-SNAPSHOT.jar meetagora-app.jar
COPY key.json /app/key.json
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "meetagora-app.jar"]