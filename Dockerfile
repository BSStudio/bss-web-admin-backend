FROM bellsoft/liberica-runtime-container:jdk-25.0.3_11-cds-musl@sha256:71ffb4cbbadfc9c9dfd9ef03222f1d95feed30fd6274e448b5e3bb9bb44e3a78 AS build
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

FROM bellsoft/liberica-runtime-container:jre-25.0.3_11-cds-musl@sha256:229ffe1882986876d2cce06238d23fb1c1434e0437af1f9a71e8f56ab4751e5d AS app
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
