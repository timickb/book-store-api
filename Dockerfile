FROM openjdk:18-alpine
EXPOSE 8080
ADD target/bookstore-api.jar bookstore-api.jar
ADD data.json data.json
ENTRYPOINT ["java", "-jar", "/bookstore-api.jar", "data.json"]