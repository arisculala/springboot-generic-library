# Spring Boot Generic Library

This document provides a detailed guide on setting up and using features provided in the generic Spring Boot Library.

## 🚀 Features

- Generic exceptions handlers
- Generic Jackson config (e.g. date format)

## 📥 Installation

- Clone the repository

```bash
git clone https://github.com/arisculala/springboot-generic-library.git
cd springboot-generic-library
```

### Use the JAR Without Publishing (Local Installation)

You can manually install the JAR in your local machine.

1. Build the generic Library JAR

```bash
mvn clean install
```

This generates (e.g. `target/springboot-generic-library-0.0.1.jar` the `version` which you can update in `pom.xml`):

2. Manually Install It in Local Maven Repository (`Note: the version value of the jar generated`)

```bash
mvn install:install-file -Dfile=target/springboot-generic-library-0.0.1.jar \
    -DgroupId=com.generic \
    -DartifactId=springboot-generic-library \
    -Dversion=0.0.1 \
    -Dpackaging=jar
```

This stores the JAR in your local Maven repository (`~/.m2/repository/`).

3. Add the Dependency in Microservices
   In the `pom.xml` of your microservice:

```bash
<dependency>
    <groupId>com.generic</groupId>
    <artifactId>springboot-generic-library</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Publish the Generic Library to Maven Repository / Github Packages

If you want to share the JAR with multiple teams/microservices, you should publish it to a Github Packages like:

1. Add Metadata in `pom.xml`
   Before publishing, update `pom.xml` to include necessary metadata.

```bash
<groupId>com.generic</groupId>
<artifactId>springboot-generic-library</artifactId>
<version>0.0.1</version>
<packaging>jar</packaging>
<name>springboot-generic-library</name>
<description>A reusable generic library for Spring Boot microservices</description>
<url>https://github.com/your-repo</url>

<licenses>
    <license>
        <name>MIT License</name>
        <url>https://opensource.org/licenses/MIT</url>
    </license>
</licenses>

<developers>
    <developer>
        <id>your-id</id>
        <name>Your Name</name>
        <email>your-email@example.com</email>
    </developer>
</developers>
```

### Using GitHub Packages

1. Create a GitHub repository for your package.
2. Add this Maven distribution management to `pom.xml`:

```bash
<distributionManagement>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/YOUR_GITHUB_USERNAME/YOUR_REPO_NAME</url>
    </repository>
</distributionManagement>
```

3. Set up your GitHub username and token in `~/.m2/settings.xml`:

```bash
<servers>
    <server>
        <id>github</id>
        <username>YOUR_GITHUB_USERNAME</username>
        <password>YOUR_GITHUB_ACCESS_TOKEN</password>
    </server>
</servers>
```

4. Publish the package:

```bash
mvn clean deploy
```

## 📝 Usage Guide

### Exception Handlers

How to use the exception library inside a microservice.

1. Use Global Exception Handler
   Since we added a `@RestControllerAdvice` in the library, it works automatically when included.

Make sure that your exception handler is inside a package that Spring Boot scans automatically.

Check Your GlobalExceptionHandler Package. Your handler is in: `com.generic.exception_library.handlers.GlobalExceptionHandler`

Ensure it's inside a package that is scanned by Spring Boot. Modify your @SpringBootApplication class:

```bash
@SpringBootApplication(scanBasePackages = {"com.trading", "com.generic.exception_library.handlers"})
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

- NOTE: If you don't want to include the package in your scan above, disable Spring Boot's Default Error Handling. Spring Boot sometimes overrides custom exception handling with its built-in error handler. To disable it, add this property in `application.yml`:

```bash
server:
  error:
    include-message: always
    include-binding-errors: always
```

For Spring Boot 3.x, use:

```bash
spring:
  mvc:
    problem-details:
      enabled: true
```

2. Throw a Custom Exception in User Service

```bash
import com.generic.exception_library.exceptions.ForbiddenException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ForbiddenException("User with email " + email + " already exists.");
        }
    }
}
```

#### Example API Response When an Exception is Thrown

If a ForbiddenException is thrown, the response will be:

Response (HTTP 400 - Bad Request)

```bash
{
    "status": 400,
    "message": "User with email user@example.com already exists.",
    "timestamp": "2025-03-05T14:30:00"
}
```

### Jackson Config

A jackson configuration is available in the library.
You can use the library for date serialization or you can update the code to add additional serialization settings.

```bash
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
```

- Configuration in `application.yml` for date

```bash
spring:
  jackson:
    date-format: yyyy-MM-dd'T'HH:mm:ss.SSSXXX
    time-zone: UTC
```

## 📜 License

This project is open-source and available under the MIT License.
