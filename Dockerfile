FROM openjdk:17
WORKDIR /app
COPY build/libs/task-tracker-bot-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-XX:MaxRAM=100M", "-jar", "app.jar"]
