# BSS Web admin backend

![CircleCI](https://img.shields.io/circleci/build/github/BSStudio/bss-web-admin-backend/main?label=build)
![GitHub Release Date](https://img.shields.io/github/release-date/BSStudio/bss-web-admin-backend)
![GitHub Tag](https://img.shields.io/github/v/tag/BSStudio/bss-web-admin-backend)
![GitHub branch checks state](https://img.shields.io/github/checks-status/BSStudio/bss-web-admin-backend/main)
![Codecov branch](https://img.shields.io/codecov/c/gh/BSStudio/bss-web-admin-backend/main)
![Swagger Validator](https://img.shields.io/swagger/valid/3.0?specUrl=https%3A%2F%2Fraw.githubusercontent.com%2FBSStudio%2Fbss-web-admin-backend%2Fmain%2Fserver%2Fweb%2Fsrc%2Fmain%2Fresources%2Fstatic%2Fopen-api.yaml)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/BSStudio/bss-web-admin-backend)
![GitHub](https://img.shields.io/github/license/BSStudio/bss-web-admin-backend)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=bugs)](https://sonarcloud.io/dashboard?id=BSStudio_bss-web-admin-backend)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=code_smells)](https://sonarcloud.io/dashboard?id=BSStudio_bss-web-admin-backend)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=BSStudio_bss-web-admin-backend)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=ncloc)](https://sonarcloud.io/dashboard?id=BSStudio_bss-web-admin-backend)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=BSStudio_bss-web-admin-backend)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=alert_status)](https://sonarcloud.io/dashboard?id=BSStudio_bss-web-admin-backend)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=BSStudio_bss-web-admin-backend)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=security_rating)](https://sonarcloud.io/dashboard?id=BSStudio_bss-web-admin-backend)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=sqale_index)](https://sonarcloud.io/dashboard?id=BSStudio_bss-web-admin-backend)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=BSStudio_bss-web-admin-backend&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=BSStudio_bss-web-admin-backend)

```mermaid
---
title: Project structure
---
classDiagram
    direction TB

     namespace server {
         class src
         class web
         class operation
         class service
         class model
         class data
         class common
     }

    integration ..> client
    client ..> operation
    integration ..> data
    web ..> operation
    web ..> service
    service ..> data
    operation ..> model
    model ..> common
    data ..> common
    service ..> model
    src ..> web
```
> Note: client can only access operation, model, common. No business code

> Note: integration can only access client (with everything mentioned above) and data. No business code

## Development

### Pre-requisites

Download sdkman to manage java and gradle versions.

```shell
git clone git@github.com:BSStudio/bss-web-admin-backend.git
cd bss-web-admin-backend
sdk env install
```

### Lint

```shell
./gradlew spotlessCheck
```

### Apply lint

```shell
./gradlew spotlessApply
```

### Test

```shell
./gradlew test
```

### Integration test

```shell
docker compose up -d
./gradlew integrationTest
docker compose down
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
