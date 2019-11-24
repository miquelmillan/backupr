# Backupr

Yet another backup tool. Intended to backup all your resources (files, facebook pics, instagram posts, etc) and have a unique place where you can find them.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

To build the software you need to following tools:

```
- Java 8
- Maven 3.6.x
- AWS s3
- Make
- Bash shell
```

### Installing

As this is a simple Spring Boot project built with Maven, to install the software it is as easy as:

```
cd backupr-cli
mvn clean package
```

once you have it build you can run it using the sample commands in the Makefile in backupr-cli folder:
```
cd backupr-cli
make dev_upload_folder
```
or
```
cd backupr-cli
make dev_download_file
```
Please be sure to have data in your S3 bucket in order to be able to download something.

## Running the tests
```
cd backupr-cli
mvn test
```
### Break down into end to end tests

Nothing defined yet.

### And coding style tests

Nothing defined yet.

## Deployment

As it is a CLI at the moment, it is as simple as running in locally. Nonetheless you will need a S3 bucket to store and retrieve the files, which can be setup in the following files:

```
src/main/resources/application.yml
```
section:

```
aws -> credentials -> bucket-name
```

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - The main application framework
* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Rules are not defined yet, but contributions are always welcome, all Pull Requests are welcome :-)

## Authors

* **Miquel Millan** - *Initial work*

## License

This project is licensed under the GPL v3 License - see the [LICENSE.md](LICENSE) file for details

## Acknowledgments

* Soon :-)
