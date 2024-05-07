# Use the openjdk:11 image as the base image
FROM openjdk:11

# Install Maven
RUN apt-get update && \
    apt-get -y install maven && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project files to the Docker image
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the Maven project
RUN mvn package

# Specify the command to run your application
CMD ["java", "-jar", "target/Weather-Station-1.0.jar"]
