FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/user-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app.jar"]
