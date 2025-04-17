FROM openjdk:17
EXPOSE 8087
ADD target/tp-foyer-0.0.1.jar tp-foyer-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/tp-foyer-0.0.1.jar"]
