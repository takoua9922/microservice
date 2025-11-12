FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy the built JAR (match actual artifact name from Maven build)
COPY target/DiscoveryServer-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app/app.jar"]
