FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN addgroup --system scm && adduser --system --ingroup scm scm

COPY --from=build /app/target/*.jar app.jar

USER scm

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
