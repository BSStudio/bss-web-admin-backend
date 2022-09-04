FROM eclipse-temurin:17.0.4.1_1-jdk-alpine as build
WORKDIR /usr/src/app
# cache dependencies
COPY ./gradlew                  ./
COPY ./settings.gradle.kts      ./
COPY ./gradle                   ./gradle/
COPY ./buildSrc/src             ./buildSrc/src/
COPY ./buildSrc/*.gradle.kts    ./buildSrc/
COPY ./web/build.gradle.kts     ./web/
COPY ./service/build.gradle.kts ./service/
COPY ./data/build.gradle.kts    ./data/
COPY ./app/build.gradle.kts     ./app/
RUN ./gradlew
# build
COPY ./ ./
ARG BUILD_ARG="bootJar --parallel"
RUN ./gradlew $BUILD_ARG

FROM eclipse-temurin:17.0.4.1_1-jre-alpine as app
# use non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
# copy jar and run it
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_JAR=$BUILD_ROOT/app/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
