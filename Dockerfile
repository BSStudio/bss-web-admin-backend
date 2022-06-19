FROM openjdk:17-jdk-slim as build
# cache dependencies
COPY ./gradlew                    ./
COPY ./settings.gradle.kts        ./
COPY ./gradle                     ./gradle/
COPY ./buildSrc/src               ./buildSrc/src/
COPY ./buildSrc/build.gradle.kts  ./buildSrc/
COPY ./web/build.gradle.kts       ./web/
COPY ./service/build.gradle.kts   ./service/
COPY ./data/build.gradle.kts      ./data/
COPY ./app/build.gradle.kts       ./app/
RUN ./gradlew
# build
COPY ./ ./
ARG BUILD_ARG="bootJar --parallel"
RUN ./gradlew $BUILD_ARG

FROM openjdk:17-jdk-slim as app
ARG BOOT_JAR=/app/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
