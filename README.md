# NexusForge

A developer collaboration and project discovery platform. Developers can create profiles,
showcase projects, publish technical posts, comment, and follow each other.

This repository is the **Spring Boot backend**. The React frontend is in a separate repository:[  ](https://github.com/infinitylimits/nexusforge-frontend.git)

## Tech stack
Java 21, Spring Boot 4, Spring Security, JWT (jjwt), Spring Data JPA / Hibernate, MySQL, Maven,
Swagger / OpenAPI (springdoc)

## Features
- JWT authentication with BCrypt password hashing
- Role-based authorization (USER, ADMIN) and ownership checks
- Projects, posts and comments with full CRUD
- Follow / unfollow system and a personalised feed
- Pagination, sorting, keyword and tech-stack filtering
- Bean Validation and global exception handling with a consistent error format
- Public developer profiles (bio, projects, posts, follower counts)
- Admin endpoints (list users, delete a user with all their data)
- Interactive API docs with Swagger UI

## Architecture
Controller -> Service -> Repository -> MySQL, with DTOs between the API and the entities.

## Run locally
1. Install JDK 21, Maven and MySQL
2. Create the database: `CREATE DATABASE nexusforge;`
3. Create `src/main/resources/application-local.properties` (this file is git-ignored):

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
app.jwt.secret=A_RANDOM_SECRET_OF_AT_LEAST_32_CHARACTERS
```

4. Run: `./mvnw spring-boot:run`
5. Open Swagger UI: http://localhost:8080/swagger-ui.html

## Main endpoints
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/register | Register |
| POST | /api/auth/login | Login, returns a JWT |
| GET, POST | /api/projects | List (paginated, filter by techStack) / create |
| PUT, DELETE | /api/projects/{id} | Update / delete (owner or admin) |
| GET, POST | /api/posts | List (search by keyword) / create |
| GET | /api/posts/feed | Posts from people you follow |
| GET, POST | /api/posts/{id}/comments | List / add comments |
| DELETE | /api/comments/{id} | Delete a comment (owner or admin) |
| POST, DELETE | /api/users/{username}/follow | Follow / unfollow |
| GET | /api/users/{username} | Public profile |
| GET | /api/admin/users | List users (admin only) |
| DELETE | /api/admin/users/{id} | Delete a user (admin only) |



## Author
Piyush Chawla
