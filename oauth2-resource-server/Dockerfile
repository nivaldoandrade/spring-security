FROM amazoncorretto:21-alpine-jdk

COPY target/*.jar app.jar

CMD apt-get update -y

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]