FROM maven:3.9.10-amazoncorretto-21 as build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src /app/src
RUN mvn clean package -DskipTests=true

FROM eclipse-temurin:21-jre-alpine

RUN ln -sf /usr/share/zoneinfo/Asia/Ho_Chi_Minh /etc/localtime

COPY --from=build /app/target/*.jar /run/app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-Xmx2048m", "-Xms256m", "-jar", "/run/app.jar"]