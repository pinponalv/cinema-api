#Compilamos el proyecto con maven y nombramos a esta etapa "build"
FROM maven:3.9-eclipse-temurin-21 AS build
#Creamos la carpeta app
WORKDIR /app
#Copiamos estos archivos
COPY pom.xml .
COPY src ./src
#Compilamos saltandonos los test, con clean limpiamos las compilaciones anteriores, package compila y genera el .jar final
RUN mvn clean package -DskipTests

#Etapa 2 imagen final, liviana solo para ejecutar la app
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

#traemos el .jar ya compilado desde la etapaba "build" y la renombro a "app.jar"
COPY --from=build /app/target/cinema-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

#comandos que se ejecutan cuando el contenedor arranca
ENTRYPOINT ["java", "-jar", "app.jar"]