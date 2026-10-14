FROM bellsoft/hardened-liberica-runtime-container:jdk-25.0.4.1_1-cds-musl@sha256:1d5baee7a254cd348fd4fa7872cd888a98512cf14d1a61aa4273a02d21d8bb55 AS build
WORKDIR /usr/src/app
# health probe for the runtime image, which has no shell, curl or wget
COPY ./docker/healthcheck/HealthCheck.java ./healthcheck/
RUN javac --release 25 -d ./healthcheck ./healthcheck/HealthCheck.java
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

FROM bellsoft/hardened-liberica-runtime-container:jre-25.0.4.1_1-cds-distroless-musl@sha256:ab1739a724cbbb4800a2f17b8a00d334956cc9d9e5b7e99534e5d8707d157e9a AS app
# distroless: no shell or package manager; run as the image's non-root appuser
USER 10001:10001
WORKDIR /home/appuser
# copy jar and run it; files stay root-owned, so the app cannot modify them
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_JAR=$BUILD_ROOT/server/build/libs/*.jar
COPY --from=build $BOOT_JAR ./app.jar
COPY --from=build $BUILD_ROOT/healthcheck/HealthCheck.class /opt/healthcheck/
HEALTHCHECK --interval=10s --timeout=5s --start-period=20s --retries=5 \
    CMD ["java", "-XX:+UseSerialGC", "-XX:TieredStopAtLevel=1", "-cp", "/opt/healthcheck", "HealthCheck", "http://localhost:8080/actuator/health"]
ENTRYPOINT ["java", "-jar", "./app.jar"]

EXPOSE 8080
