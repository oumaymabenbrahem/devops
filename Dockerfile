# Use Maven with OpenJDK 17
FROM maven:3.8.4-openjdk-17

# Set working directory
WORKDIR /app

# Copy the whole project into the container
COPY . .

# Build the project using Maven
RUN mvn clean install -DskipTests

# Expose application port
EXPOSE 8083

# Run the generated jar
ENTRYPOINT ["java", "-jar", "target/tp-foyer-0.0.1.jar"]
