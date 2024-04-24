
## Assignment

### Prerequisite Installation
- java version "21.0.3"
- temporal
- docker
- Stripe key

### Run
- Locate the docker compose file and run the following command
```sh
sudo docker-compose up -d
```

- locate the temporal.exe file and run the following command
```sh
temporal server start-dev
```
- to build the app locate the gradle.build file and run the following command
```sh
gradlew build
```
- Inside   `<PATH>\build\libs` locate the jar file and run the following command
```sh
java -jar app-0.0.1-SNAPSHOT.jar
```
- to execute the test case, use following command, the report can be find at: `<PATH>/build/reports/tests/test` execute `index.html`

```sh
gradlew test
```
- to execute the sonar lint report, use following command, the report can be find at: `<PATH>/build/reports/sonarlint` execute `report.html`
```sh
gradlew sonarlintMain
```
- to format the code, use following command
```sh
gradlew spotlessApply
```

#### Video Explanation

1. Prerequisite Installations:
   https://www.loom.com/share/c7623285a092440ebd90ab411284a516
2. App Build & Execution
   https://www.loom.com/share/1083f1fee61341fba7b960a217011cc6
3. Post, Patch & Get APIs
   https://www.loom.com/share/79052106e83442158fbedf86dd990f73
4. Test & Lint Coverages
   https://www.loom.com/share/ce70ab99480c44a8b7b3d934e1609498
5. Temporal Server
   https://www.loom.com/share/ce9a3b675de945769599b66a3ad37324
6. Codebase
   https://www.loom.com/share/870527765dd7408093d737d38d87c064

#### Limitation
- the springboot app is not dockerized.
- the containers created for temporal server via docker compose, available on github was crashing `https://github.com/temporalio/docker-compose/blob/main/docker-compose.yml`, hence prefered to use local setup
