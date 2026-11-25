FROM bellsoft/hardened-liberica-runtime-container:jdk-25.0.5.1-cds-musl@sha256:dbeba3487bc63040564800e8fb18c44e099b83bef88c7cb4fab3d3e0e51121ea AS build
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
COPY ./server/ui/build.gradle.kts        ./server/ui/
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

FROM bellsoft/hardened-liberica-runtime-container:jre-25.0.5.1-cds-distroless-musl@sha256:78b827dcba0de56defb7e461c371ab3726bdc8f9f8b396756406c8e9c948e650 AS app
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
