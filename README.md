# Java SFTP with SSH key example

## Run the tests

Will start up a SFTP server in a container using [testcontainers](https://testcontainers.com/) and connect to it with a
SSH key.

```shell
./gradlew test
```

## Run the App

Will connect to a SFTP server and fetch one file and print the file contents.

Requires that a SFTP server is running locally. Read how to start it [here](sftp/README.md).

```shell
./gradlew run
```

