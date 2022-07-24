# BSS Web admin backend
![CircleCI](https://img.shields.io/circleci/build/github/BSStudio/bss-web-admin-backend/main?label=build)
![GitHub branch checks state](https://img.shields.io/github/checks-status/BSStudio/bss-web-admin-backend/main)
![Codecov branch](https://img.shields.io/codecov/c/gh/BSStudio/bss-web-admin-backend/main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=BSStudio_bss-web-admin-backend)
![Swagger Validator](https://img.shields.io/swagger/valid/3.0?specUrl=https%3A%2F%2Fraw.githubusercontent.com%2FBSStudio%2Fbss-web-admin-backend%2Fmain%2Fweb%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fopen-api.yaml)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/BSStudio/bss-web-admin-backend)
![Docker Image Size (tag)](https://img.shields.io/docker/image-size/csikb/bss-web-admin-backend/latest)
![GitHub](https://img.shields.io/github/license/BSStudio/bss-web-admin-backend)

## Development
### Lint
```shell
./gradlew ktlintCheck
```
### Test
```shell
./gradlew test
```
### Build
Docker:
```shell
docker build -t bss-web-admin-backend .
```
Gradle:
```shell
./gradlew build
```
### Run
Docker:
```shell
docker run bss-web-admin-backend
```
Docker compose:
```shell
docker compose up
```
Gradle:
```shell
./gradlew bootRun
```
