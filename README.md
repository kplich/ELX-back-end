# ELX - back end

Back end for ELX application written in Spring Boot with Kotlin.

## Running the application locally (dev profile)

The application requires Java 11.

Copy `example-application-dev.properties` and `example-data.sql` to the `main/resources/` folder and rename them to `application-dev.properties` and `data.sql`, respectively.
Edit the `application-dev.properties` file with details about your database driver, connection, secrets; adjust the configuration to your needs.

Use the default data initialization script or fill it with your own data.
To use the application fully, you'll certainly have to change the users' Ethereum addresses.
The script provides four accounts:
- username: jasmine03, password: maybE4$$$
- username: maryjann password: Maryjann1!
- username: jerrybumbleberry, password: Jerrry1!
- username: NewKidOnTheBlock, password: NewKidd0)

On Windows, run:
```shell script
gradlew.bat bootRunDev
```

On Linux, run:
```shell script
./gradlew bootRunDev
```
The application will be available under `http://localhost:8080`.

## Running the application in production

Define environment variables `DATABASE_URL` and `JWT_SECRET`.
On Windows, run:
```shell script
gradlew.bat bootRun
```

On Linux, run:
```shell script
./gradlew bootRun
```
The application will be available under `http://localhost:8080`.

## Packaging the application
Using the Gradle wrapper (the `gradlew` file), run the `bootJar` task. The resulting JAR file will be placed in `build/libs`.

## Running the tests
Using the Gradle wrapper, run the `test` task.

## Display other build/run tasks
Running `./gradlew tasks` will output all available tasks.