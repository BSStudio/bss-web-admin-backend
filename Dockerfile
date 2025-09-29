FROM bellsoft/liberica-runtime-container:jdk-25-cds-musl AS build
WORKDIR /usr/src/app
# cache dependencies
COPY ./gradlew                         ./
COPY ./gradle.properties               ./
COPY ./gradle/wrapper                  ./gradle/wrapper/
RUN ./gradlew
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
COPY ./integration/build.gradle.kts    ./integration/
COPY ./client/build.gradle.kts         ./client/
# build
COPY ./buildSrc ./buildSrc
COPY ./server   ./server
ARG BUILD_ARG="bootJar"
RUN ./gradlew ${BUILD_ARG}

FROM bellsoft/liberica-runtime-container:jre-25-cds-musl@sha256:dfba02c9de055cd89f1c3e860425bef21cd88129a49a874e6024b953d9060d05 AS app
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
