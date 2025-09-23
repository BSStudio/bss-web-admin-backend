FROM bellsoft/liberica-runtime-container:jdk-21.0.8_13-crac-cds-musl@sha256:38fc3826ce2ef81058b4384d4d49a33ceba57607737a775af369db66daeb6009 AS build
WORKDIR /usr/src/app
# cache dependencies
COPY ./gradlew                         ./
COPY ./gradle.properties               ./
COPY ./gradle                          ./gradle/
COPY ./settings.gradle.kts             ./
COPY ./buildSrc/src                    ./buildSrc/src/
COPY ./buildSrc/*.gradle.kts           ./buildSrc/
COPY ./server/build.gradle.kts         ./server/
COPY ./server/web/build.gradle.kts     ./server/web/
COPY ./server/service/build.gradle.kts ./server/service/
COPY ./server/data/build.gradle.kts    ./server/data/
COPY ./server/model/build.gradle.kts   ./server/model/
COPY ./server/common/build.gradle.kts  ./server/common/
COPY ./integration/build.gradle.kts    ./integration/
COPY ./client/build.gradle.kts         ./client/
COPY ./server   ./server
ARG BUILD_ARG="bootJar"
RUN ./gradlew ${BUILD_ARG}

FROM bellsoft/liberica-runtime-container:jre-21.0.8_12-cds-musl@sha256:8e63ca393ad6f5e39734c742cc5632bff702450e632684880232ce3057ed1831 AS app
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
