# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=/workspace/target/catalogue-service-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} /app/app.jar

ENV JAVA_OPTS=""
WORKDIR /app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
