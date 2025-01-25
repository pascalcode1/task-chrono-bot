FROM eclipse-temurin:17
WORKDIR /app
COPY build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
ENTRYPOINT ["java", "-XX:MaxRAM=100M", "-jar", "app.jar"]
