# Newton Flix

[![CircleCI](https://circleci.com/gh/nathanberry/newton-flix.svg?style=shield)](https://circleci.com/gh/nathanberry/newton-flix)
[![Heroku](https://img.shields.io/badge/Heroku-Deployed-brightgreen.svg)](http://newton-flix.herokuapp.com/)

Everyone loves movies, but I wonder how many movies about Newton exist?

## TL;DR;
[Angular 4](https://angular.io/) frontend using [Bootstrap 4](https://getbootstrap.com/) over a [Spring Boot](https://projects.spring.io/spring-boot/) backend.  [CircleCI](https://circleci.com/) used for continuous integration, and automatically deployed to [Heroku](http://newton-flix.herokuapp.com/) on success.

## Getting Started

You can checkout the live version of the web application on [Heroku](http://newton-flix.herokuapp.com/).  Note that the first attempt may take a little time, since the app is being hosted on a free account (Heroku keeps free apps off when not in use).

If you would like to build and run the application locally, follow the instructions below.

```bash
git clone https://github.com/nathanberry/newton-flix.git
cd newton-flix
mvn clean install
java -Dserver.port=9090 -jar target/newton-flix-1.0.0.war
```

**_Note:_** The `-Dserver.port=9090` can be omitted, at which time the default port will be `8080`

Once running, open your browser to [http://localhost:9090](http://localhost:9090).

## Project Details

The project is built using Maven and makes use of several plugins to help with building and packaging the frontend Angular application, executing static code analysis, and code coverage.

### Structure
For the most part the project is a standard Maven Java server application based project, with the exception of the Angular application source.

The Angular application code is stored under the `src/main/client` folder alongside the `java` folder that holds the server code. 

```bash
newton-flix
|--src
|   |--main
|   |   |--client
|   |   |
|   |   |  Contains the angular 4 app
|   |   |
|   |   |--java
|   |   |
|   |   |  Contains server side code for REST API
|   |   |
|   |--test
|   |   |--java
|   |   |
|   |   |  Contains server tests
|   |   |
|--pom.xml
```

### Backend
The server is built as a Spring Boot application.  This provides us the ability to be self-executing from the command line, as well as deployable as a WAR.

There is one REST API to service the application that is defined by `MoviesController`.  The one endpoint available can be tested independently from the UI by issuing the following request.

```bash
curl http://localhost:9090/api/movies/?search=Newton&page=1
```
**_Note:_** Both parameters `search` and `page` are optional, above are the defaults.

#### Maven Plugins
##### Frontend Plugin

Allows us to run node based process to build, test, and package our client application.

The plugin is configured to use a particular version of node and npm, and manages the process of dynamically installing the components if they are not already present in the build environment.

Once the node and npm dependencies have been resolved, the plugin has been configured to execute the client build and test processes, with the test execution tied to the `skipTests` option so they can be ignored when built and deployed to Heroku.

###### Plugin Snippet:
```xml
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.4</version>
    <executions>
        <execution>
            <id>install node and npm</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>install-node-and-npm</goal>
            </goals>
            <configuration>
                <nodeVersion>v8.4.0</nodeVersion>
                <npmVersion>5.3.0</npmVersion>
                <nodeDownloadRoot>http://nodejs.org/dist/</nodeDownloadRoot>
                <npmDownloadRoot>http://registry.npmjs.org/npm/-/</npmDownloadRoot>
            </configuration>
        </execution>
        <execution>
            <id>npm install</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>npm</goal>
            </goals>
        </execution>
        <execution>
            <id>npm run build</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>run build</arguments>
            </configuration>
        </execution>
        <execution>
            <id>npm run test-with-coverage</id>
            <phase>generate-resources</phase>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>run test-with-coverage</arguments>
                <skip>${skipTests}</skip>
            </configuration>
        </execution>
    </executions>
    <configuration>
        <workingDirectory>${project.basedir}/src/main/client</workingDirectory>
    </configuration>
</plugin>
```

##### Resources Plugin
The Maven resources plugin is used to copy the client application build output to the correct location for packaging with the server.

###### Plugin Snippet:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version>3.0.1</version>
    <executions>
        <execution>
            <id>Copy React app output</id>
            <phase>process-resources</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>${basedir}/target/classes/static</outputDirectory>
                <resources>
                    <resource>
                        <directory>${basedir}/src/main/client/dist</directory>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
```
##### PMD and FindBugs
[PMD](https://pmd.github.io/) and [FindBugs](http://findbugs.sourceforge.net/) are both run during build to help catch common coding errors.
Any errors found by either of these plugins will fail the build, which helps with preventing technical debt.

##### Jacoco
The [Jacoco](http://www.eclemma.org/jacoco/) plugin is used to report on code coverage during Java test execution.

The code coverage report can be viewed after a build by opening the following file `target/site/jacoco/index.html`.

### Frontend
The frontend is built on the Angular 4 based angular-cli approach to defining the base structure and build of the client application.  This process is built on top of node and comes with a set of predefined scripts to assist with the build, test, and packaging.

Scripts defined in the `package.json` file are used by the Maven frontend plugin mentioned above to build and test.

During development, you can run the client from the command line in a mode that allows you to iterate quickly over changes and avoid the package step to bundle the client and server.
When run in this mode, some additional configuration was done to enable the client to proxy API requests to the development server, which I would typically run in debug mode from IntelliJ.

Starting the development client server
```bash
cd src/main/client
npm start
```

The following file was added to enable the proxy between client dev server and the server based dev server hosting the REST API.

###### proxy.conf.json
```json
{
  "/api": {
    "target": "http://localhost:9090",
    "secure": false
  }
}
```
This process is setup to watch for changes, so as you save changes in the client code you see them immediately in your browser and get a quick turnaround as you are developing.

#### Application Code
The code representing the client application is found primarily under the `src/main/client/src/app` folder.

There is a service implementation, `movies.service.ts`, whose job is to retrieve movie search results from the server REST API.
This service is `Injectable` and is used by the one component used to render the application `app.component.ts`.

##### Testing
There is a test to verify some behavior of the component that is defined in the `app.component.spec.ts` file.  The following command can be executed to run the tests and generate a coverage report.

```bash
# Execute from the client directory
npm run test-with-coverage
```

The coverage report can be viewed by opening the `coverage/index.html` file that is generated.

The Angular CLI also provides some test infrastructure to execute end-to-end (e2e) tests that are built using a library called [Protractor](http://www.protractortest.org/), which uses [Selenium WebDriver](http://www.seleniumhq.org/projects/webdriver/) as a way to drive UI based tests against a fully deployed application.
I wanted to explore the e2e capability, but didn't have time to do so.
