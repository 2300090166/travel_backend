# TravelEase Backend (Spring Boot)

This is a starter Spring Boot backend for the TravelEase frontend.

Key points:
- Java 17, Spring Boot 2.7.18
- Maven project with WAR packaging (suitable for deploying to external Tomcat 9)
- MySQL datasource (configure values in `src/main/resources/application.properties`)
- Simple authentication endpoints: `/api/auth/signin`, `/api/auth/signup`
- On startup the app seeds an admin user from properties `app.admin.username` / `app.admin.password` with role `ADMIN`.

How to run locally (with embedded Tomcat disabled in external container builds):

1. Configure `src/main/resources/application.properties` with your MySQL credentials.
2. Build the war: `mvn clean package` (generates `target/travelease-backend-0.0.1-SNAPSHOT.war`).
3. Deploy the WAR to Tomcat 9 (drop into `webapps`) or run with `mvn spring-boot:run` for a quick run.

Notes:
- Passwords are stored in plaintext in this example for clarity; replace with hashing (BCrypt) for production.
- Mail is included as a dependency; configure SMTP settings prior to using.
