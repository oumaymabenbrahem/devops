FROM openjdk:17
EXPOSE 8087
ADD target/tp-foyer-0.0.1 tp-foyer-0.0.1
ENTRYPOINT ["java", "-jar", "/tp-foyer-0.0.1"]
