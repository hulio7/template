FROM maven:3.8.4-openjdk-17

COPY ./target/template-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "/app.jar"]