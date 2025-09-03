FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/product-service-*.jar app.jar
EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/app/app.jar"]

