FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
COPY . .
RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:21-jdk-slim
EXPOSE 8080
COPY --from=build target/roteiro01-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


FROM openjdk:11-jdk-slim
VOLUME /tmp
COPY target/myapp.jar myapp.jar
ENTRYPOINT ["java","-jar","/myapp.jar"]

# Variáveis de ambiente para PostgreSQL
ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_NAME=mydatabase
ENV DB_USERNAME=myusername
ENV DB_PASSWORD=mypassword