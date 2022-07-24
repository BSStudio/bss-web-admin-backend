# BSS Web admin backend [![CircleCI](https://dl.circleci.com/status-badge/img/gh/BSStudio/bss-web-admin-backend/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/BSStudio/bss-web-admin-backend/tree/main) [![codecov](https://codecov.io/gh/BSStudio/bss-web-admin-backend/branch/main/graph/badge.svg?token=RJJ8JE7E5H)](https://codecov.io/gh/BSStudio/bss-web-admin-backend)
[![CircleCI](https://dl.circleci.com/insights-snapshot/gh/BSStudio/bss-web-admin-backend/main/all/badge.svg?window=90d)](https://app.circleci.com/insights/github/BSStudio/bss-web-admin-backend/workflows/all/overview?branch=main&reporting-window=last-90-days&insights-snapshot=true)
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
