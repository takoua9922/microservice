FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/avis-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
# Par défaut profil normal; change au besoin:
ENTRYPOINT ["java","-jar","/app/app.jar"]
