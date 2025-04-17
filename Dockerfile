FROM maven:3.8.4-openjdk-17
EXPOSE 9001
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]