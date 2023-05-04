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
COPY ./server/model/build.gradle.kts   ./server/model/
COPY ./server/common/build.gradle.kts  ./server/common/
RUN ./gradlew
# build
COPY ./ ./
ARG BUILD_ARG="bootWar --parallel"
RUN ./gradlew $BUILD_ARG

FROM tomcat:11.0.0-jre17-temurin-jammy AS app

RUN addgroup --system tomcat && \
    adduser --system --ingroup tomcat tomcat && \
    chown -R tomcat:tomcat $CATALINA_HOME
USER tomcat:tomcat

ARG BUILD_ROOT=/usr/src/app
ARG BOOT_WAR=$BUILD_ROOT/server/build/libs/*.war
COPY --from=build $BOOT_WAR $CATALINA_HOME/webapps/ROOT.war

LABEL org.opencontainers.image.source="https://github.com/BSStudio/bss-web-admin-backend"
LABEL org.opencontainers.image.description="BSS Web admin backend"
LABEL org.opencontainers.image.licenses="GPL-3.0"
