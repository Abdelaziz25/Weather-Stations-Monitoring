FROM maven:3.9.2-eclipse-temurin-17-alpine

WORKDIR /weather-station
EXPOSE 8080
COPY src ./src
COPY pom.xml .

RUN mvn clean install

CMD ["mvn","exec:java"]