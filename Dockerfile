FROM maven:3.6.0-jdk-11-slim as maven
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=maven ./target/*.jar ./
EXPOSE 8101
CMD ["java", "-jar", "./user-service-1.0-SNAPSHOT.jar"]