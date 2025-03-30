FROM openjdk:21
WORKDIR /app
COPY build/libs/FindFlagBot-0.0.1-SNAPSHOT-plain.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]