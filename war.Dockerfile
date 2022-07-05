FROM eclipse-temurin:17-jdk-alpine as build
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
ARG BUILD_ARG="bootWar --parallel"
RUN ./gradlew $BUILD_ARG

FROM tomcat:9-jre17
ARG BUILD_ROOT=/usr/src/app
ARG BOOT_WAR=$BUILD_ROOT/app/build/libs/*.war
COPY --from=build $BOOT_WAR $CATALINA_HOME/webapps/ROOT.war
