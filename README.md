# springboot-exception-library

A reusable exception handling library for Spring Boot microservices

## Use the JAR Without Publishing (Local Installation)

You can manually install the JAR in your local machine.

1️⃣ Build the Exception Library JAR

```bash
mvn clean install
```

This generates (e.g. version=0.0.1):

```bash
target/springboot-exception-library-0.0.1.jar
```

2️⃣ Manually Install It in Local Maven Repository

```bash
mvn install:install-file -Dfile=target/springboot-exception-library-0.0.1.jar \
    -DgroupId=com.generic \
    -DartifactId=springboot-exception-library \
    -Dversion=0.0.1 \
    -Dpackaging=jar
```

This stores the JAR in your local Maven repository (`~/.m2/repository/`).

3️⃣ Add the Dependency in Microservices
In the `pom.xml` of your microservice:

```bash
<dependency>
    <groupId>com.generic</groupId>
    <artifactId>springboot-exception-library</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Publish the Exception Library to Maven Repository / Github Packages

If you want to share the JAR with multiple teams/microservices, you should publish it to a Github Packages like:

1️⃣ Add Metadata in `pom.xml`
Before publishing, update `pom.xml` to include necessary metadata.

```bash
<groupId>com.generic</groupId>
<artifactId>springboot-exception-library</artifactId>
<version>0.0.1</version>
<packaging>jar</packaging>
<name>springboot-exception-library</name>
<description>A reusable exception handling library for Spring Boot microservices</description>
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

✅ Using GitHub Packages

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

## Usage in a Microservice

How to use the exception library inside a microservice.

1️⃣ Use Global Exception Handler
Since we added a `@RestControllerAdvice` in the library, it works automatically when included.

2️⃣ Throw a Custom Exception in User Service

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

# Example API Response When an Exception is Thrown

If a ForbiddenException is thrown, the response will be:

Response (HTTP 400 - Bad Request)

```bash
{
    "status": 400,
    "message": "User with email user@example.com already exists.",
    "timestamp": "2025-03-05T14:30:00"
}
```
