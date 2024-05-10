FROM maven:3.9.2-eclipse-temurin-17-alpine
EXPOSE 8080

WORKDIR /dataAcquisition

COPY src ./src
COPY pom.xml .

RUN mvn clean install

CMD ["mvn","exec:java"]