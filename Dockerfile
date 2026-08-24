FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./

RUN chmod +x gradlew

COPY src src

RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup --system --gid 1001 appgroup && \
    adduser --system --uid 1001 --ingroup appgroup appuser

COPY --from=build /app/build/libs/*.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080
EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]