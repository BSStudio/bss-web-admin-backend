FROM eclipse-temurin:17.0.4.1_1-jdk-alpine as build
WORKDIR /usr/src/app
# cache dependencies
COPY ./gradlew                         ./
COPY ./settings.gradle.kts             ./
COPY ./gradle                          ./gradle/
COPY ./buildSrc/src                    ./buildSrc/src/
COPY ./buildSrc/*.gradle.kts           ./buildSrc/
COPY ./server/build.gradle.kts         ./server/
COPY ./server/web/build.gradle.kts     ./server/web/
COPY ./server/service/build.gradle.kts ./server/service/
COPY ./server/data/build.gradle.kts    ./server/data/
RUN ./gradlew
# build
COPY ./ ./
ARG BUILD_ARG="bootWar --parallel"
RUN ./gradlew $BUILD_ARG

FROM tomcat:9.0.68-jre17-temurin-jammy as app
RUN apt-get install curl
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_WAR=$BUILD_ROOT/server/build/libs/*.war
COPY --from=build $BOOT_WAR $CATALINA_HOME/webapps/ROOT.war
