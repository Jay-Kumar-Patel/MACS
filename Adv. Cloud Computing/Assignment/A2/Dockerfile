FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.properties application.properties
ENTRYPOINT ["java","-jar", "-Dspring.config.location=file:application.properties", "app.jar"]