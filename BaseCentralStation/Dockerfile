FROM maven:3.9.2-eclipse-temurin-17-alpine

WORKDIR /CentralServer
EXPOSE 8080
# Copy the Maven project files into the Docker container
COPY src ./src
COPY pom.xml .

# Copy the Avro schema file into the Docker container
COPY src/main/java/BaseCentralStation/weather_record.avsc /CentralServer/src/main/java/BaseCentralStation/weather_record.avsc

# Build the Maven project inside the Docker container
RUN mvn clean install

# Specify the command to run your Java application
CMD ["mvn", "exec:java"]
