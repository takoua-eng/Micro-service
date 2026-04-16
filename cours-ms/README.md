# cours-ms

Spring Boot microservice for **Course Management** (MS Cours). Independent service; teacher validation via user/teacher-service will be added later.

## Stack

- **Java 17**, **Maven**, **Spring Boot 3.2**
- **MySQL** (XAMPP)
- **Spring Data JPA**, **Spring Web**, **Validation**
- **Spring Cloud OpenFeign** (synchronous calls to user-ms)
- **Eureka Client** (service discovery)

## Setup

1. **MySQL (XAMPP)**  
   Start MySQL and create the database:
   ```sql
   CREATE DATABASE cours_db;
   ```

2. **Configuration**  
   Edit `src/main/resources/application.properties` if needed (port `8082`, user `root`, empty password by default).

3. **Run**
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints (Course)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/courses` | Create course |
| GET | `/api/courses` | Get all courses |
| GET | `/api/courses/{id}` | Get course by id |
| PUT | `/api/courses/{id}` | Update course |
| DELETE | `/api/courses/{id}` | Delete course |
<!-- Teacher-related endpoints removed; course is no longer linked directly to a teacher. -->

## API Endpoints (OpenFeign user-ms)

All these endpoints are exposed by **cours-ms**, but internally call **user-ms** using **OpenFeign**:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/cours/users` | Get all users from user-ms |
| GET | `/cours/users/{id}` | Get user by id from user-ms |
| POST | `/cours/{id}/favorite-users/{userId}` | Add `userId` to favorites of course `{id}` |
| GET | `/cours/{id}/favorite-users` | Get favorite users (from user-ms) for course `{id}` |

## Course payload (create/update)

```json
{
  "title": "Algorithms",
  "description": "Introduction to algorithms",
  "credits": 6,
  "semester": "S3"
}
```

`title` is required; other fields are optional.
