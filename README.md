# BSS Web admin backend
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
