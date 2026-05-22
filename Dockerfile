FROM bellsoft/liberica-runtime-container:jdk-25.0.3_11-cds-musl@sha256:cdd8a256ffc696b0244afb8c0ba9e862657d7721ba91b7f6ffd1b1784c710a4c AS build
WORKDIR /usr/src/app
# cache dependencies
COPY ./buildSrc/*.gradle.kts             ./buildSrc/
COPY ./buildSrc/src                      ./buildSrc/src/
COPY ./client/build.gradle.kts           ./client/
COPY ./gradle                            ./gradle/
COPY ./integration/build.gradle.kts      ./integration/
COPY ./server/build.gradle.kts           ./server/
COPY ./server/client/build.gradle.kts    ./server/client/
COPY ./server/common/build.gradle.kts    ./server/common/
COPY ./server/data/build.gradle.kts      ./server/data/
COPY ./server/model/build.gradle.kts     ./server/model/
COPY ./server/operation/build.gradle.kts ./server/operation/
COPY ./server/service/build.gradle.kts   ./server/service/
COPY ./server/web/build.gradle.kts       ./server/web/
COPY ./gradlew                           ./
COPY ./gradle.properties                 ./
COPY ./settings.gradle.kts               ./
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew
# build
COPY ./buildSrc ./buildSrc
COPY ./server   ./server
ARG BUILD_ARG="bootJar"
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew ${BUILD_ARG}

FROM bellsoft/liberica-runtime-container:jre-25.0.3_11-cds-musl@sha256:6edf3c8b1c1a4f5afb55961fa629e8aed117a3e2d92f2b1781cbf612220bf2c9 AS app
# use non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /home/spring
# copy jar and run it
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_JAR=$BUILD_ROOT/server/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]

EXPOSE 8080
