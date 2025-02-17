FROM bellsoft/liberica-openjdk-alpine-musl:21.0.6 AS build
WORKDIR /usr/src/app
# cache dependencies
COPY ./gradlew                         ./
COPY ./gradle.properties               ./
COPY ./settings.gradle.kts             ./
COPY ./gradle                          ./gradle/
COPY ./buildSrc/src                    ./buildSrc/src/
COPY ./buildSrc/*.gradle.kts           ./buildSrc/
COPY ./server/build.gradle.kts         ./server/
COPY ./server/web/build.gradle.kts     ./server/web/
COPY ./server/service/build.gradle.kts ./server/service/
COPY ./server/data/build.gradle.kts    ./server/data/
COPY ./server/model/build.gradle.kts   ./server/model/
COPY ./server/common/build.gradle.kts  ./server/common/
RUN ./gradlew
# build
COPY ./buildSrc ./buildSrc
COPY ./server   ./server
ARG BUILD_ARG="bootJar --parallel"
RUN ./gradlew ${BUILD_ARG}

FROM bellsoft/liberica-openjre-alpine-musl:21.0.6 AS app
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
LABEL org.opencontainers.image.source="https://github.com/BSStudio/bss-web-admin-backend"
LABEL org.opencontainers.image.description="BSS Web admin backend"
LABEL org.opencontainers.image.licenses="GPL-3.0"
