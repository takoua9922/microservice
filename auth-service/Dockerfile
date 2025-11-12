FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Extract the JAR file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the dependencies and application from build stage
COPY --from=0 /app/target/dependency/BOOT-INF/lib /app/lib
COPY --from=0 /app/target/dependency/META-INF /app/META-INF
COPY --from=0 /app/target/dependency/BOOT-INF/classes /app

# Expose port
EXPOSE 8082

# Set the entrypoint
ENTRYPOINT ["java", "-cp", "/app:/app/lib/*", "com.esprit.ms.auth.AuthServiceApplication"]
