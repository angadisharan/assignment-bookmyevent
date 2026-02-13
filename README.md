# BookMyEvent (modular monolith)

Kotlin + Spring Boot sample application scaffold for the BookMyEvent project.

Prereqs
- JDK 21 (or install Temurin 21)
- Gradle (wrapper included) — use `./gradlew` from project root
- MySQL 8.x

Install a JDK 21 (macOS, Homebrew example)
```bash
brew install --cask temurin21
export JAVA_HOME=$(/usr/libexec/java_home -v21)
java -version
```

Run with Gradle
```bash
./gradlew bootRun
```

Database setup (one-time, development)
- This project includes a one-time destructive schema creation script for fresh local/dev databases:
  - `/db/ddl/create_schema.sql`
- The script drops and recreates the `bookmyevent` database and creates a developer user (`dev` / `dev123`) for convenience.
- To apply the schema:
```bash
# start mysql server (example via Homebrew)
brew services start mysql

# run the create script as root (or a user with CREATE USER / CREATE DATABASE privileges)
mysql -u root -p < db/ddl/create_schema.sql
```

Configuration
- The application datasource is configured in `src/main/resources/application.yml`. By default it points at:
  `jdbc:mysql://localhost:3306/bookmyevent` and you can use the developer user created by the script:
  username: `dev`, password: `dev123`

Notes and safety
- The provided SQL script is destructive and intended for local development only. Do NOT run it against production.
- Passwords in the demo script are plaintext for convenience — switch to secure secrets management for real deployments.

Next steps
- Implement services, repositories and REST APIs (skeleton modules are present under `src/main/kotlin/com/bookmyevent/`).
- Consider adding Flyway/Liquibase if you want proper non-destructive migrations.
