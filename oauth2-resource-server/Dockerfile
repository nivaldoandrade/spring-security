FROM maven:3.9.9-amazoncorretto-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:21-alpine-jdk

COPY target/*.jar app.jar

CMD apt-get update -y

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]