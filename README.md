# Report Service

![Release](https://github.com/AMPnet/report-service-eth/workflows/Release/badge.svg?branch=master) [![codecov](https://codecov.io/gh/AMPnet/report-service-eth/branch/master/graph/badge.svg)](https://codecov.io/gh/AMPnet/report-service-eth)


Report service is a part of the AMPnet crowdfunding project on Ethereum blockchain.

Service must have running and initialized database. Default database url is `locahost:5432`.
To change database url set configuration: `spring.datasource.url` in file `application.properties`.
To initialize database run script in the project root folder:

```sh
./initialize-local-database.sh
```

## Start

Application is running on port: `8137`. To change default port set configuration: `server.port`.

### Build

```sh
./gradlew build
```

### Run

```sh
./gradlew bootRun
```

After starting the application, API documentation is available at: `localhost:8137/docs/index.html`.
If documentation is missing generate it by running gradle task:

```sh
./gradlew copyDocs
```

### Test

```sh
./gradlew test
```

## Application Properties

### JWT

Set public key property to verify JWT: `com.ampnet.reportserviceeth.jwt.public-key`
