# BSS Web admin backend
![CircleCI](https://img.shields.io/circleci/build/github/BSStudio/bss-web-admin-backend/main?label=build)
![GitHub branch checks state](https://img.shields.io/github/checks-status/BSStudio/bss-web-admin-backend/main)
![Codecov branch](https://img.shields.io/codecov/c/gh/BSStudio/bss-web-admin-backend/main)

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
