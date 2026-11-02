FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

ENV AWS_REGION=us-east-2
ENV AWS_BUCKET_NAME=qrcode-apphsp

ENTRYPOINT ["java", "-jar", "app.jar"]