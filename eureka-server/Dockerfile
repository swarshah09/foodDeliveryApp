FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/eureka-server-*.jar app.jar
EXPOSE 8761
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/app/app.jar"]

FROM eclipse-temurin:17-jdk as builder
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
ENV JAVA_OPTS=""
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

