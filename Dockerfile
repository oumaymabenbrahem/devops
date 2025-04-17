FROM openjdk:17
EXPOSE 8087
ADD target/foyer-3.0.0.jar foyer-3.0.0.jar
ENTRYPOINT ["java","-jar","/foyer-3.0.0.jar"]