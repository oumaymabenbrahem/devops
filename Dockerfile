FROM maven:3.8.4-openjdk-17
WORKDIR /foyer
EXPOSE 8083
ADD target/tp-foyer-0.0.1-SNAPSHOT.jar tp-foyer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/tp-foyer-0.0.1-SNAPSHOT.jar"]
