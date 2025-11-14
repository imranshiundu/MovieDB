# Movies API - Complete REST API for Movie Database Management

## Project Overview

A robust Spring Boot REST API for managing a movie database with full CRUD operations, advanced filtering, caching, metrics monitoring, and comprehensive relationship management between movies, genres, and actors. Built with modern Java technologies and following RESTful principles.

## Project Structure

```
movies-api/
│
├── src/main/
│   ├── java/com/example/moviesapi/
│   │   ├── MoviesApiApplication.java              # Main Spring Boot application
│   │   ├── model/
│   │   │   ├── Genre.java                         # Genre entity (id, name)
│   │   │   ├── Actor.java                         # Actor entity (id, name, birthDate)
│   │   │   └── Movie.java                         # Movie entity (id, title, releaseYear, duration)
│   │   ├── repository/
│   │   │   ├── GenreRepository.java
│   │   │   ├── ActorRepository.java
│   │   │   └── MovieRepository.java
│   │   ├── service/
│   │   │   ├── GenreService.java
│   │   │   ├── ActorService.java
│   │   │   └── MovieService.java
│   │   ├── controller/
│   │   │   ├── GenreController.java
│   │   │   ├── ActorController.java
│   │   │   └── MovieController.java
│   │   ├── exception/
│   │   │   ├── ResourceNotFoundException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── dto/
│   │   │   ├── MovieRequest.java
│   │   │   ├── ActorRequest.java
│   │   │   ├── GenreRequest.java
│   │   │   ├── MovieResponse.java
│   │   │   ├── ActorResponse.java
│   │   │   └── GenreResponse.java
│   │   ├── config/
│   │   │   ├── DatabaseConfig.java
│   │   │   └── SwaggerConfig.java
│   │   └── postman/
│   │       └── MoviesAPI.postman_collection.json  # Postman collection for API testing
│   │
│   ├── resources/
│   │   ├── application.properties                 # Application configuration
│   │   └── data.sql                               # Sample data
│
├── movies.db                                      # SQLite database file (auto-created)
├── pom.xml                                        # Maven dependencies
├── README.md                                      # Documentation and usage guide
├── structure.txt                                  # Project structure reference
```

## Technologies Used

| Technology            | Version      | Purpose                    |
|-----------------------|-------------|----------------------------|
| Java                  | 17+         | Programming language       |
| Spring Boot           | 3.2.0       | Backend application        |
| Spring Data JPA       | 3.2.0       | Database ORM               |
| SQLite                | 3.41+       | Relational database        |
| sqlite-jdbc           | 3.41.2.1    | JDBC driver for SQLite     |
| Hibernate             | 6.3.1       | ORM provider               |
| Maven                 | 3.8+        | Build and dependency mgmt  |
| Jakarta Validation    | 3.0+        | Bean Validation            |
| Spring Web            | 6.1.1       | REST API endpoints         |
| Swagger UI/OpenAPI    | 1.7+        | API documentation          |
| Postman               | -           | API testing                |

## Quick Start Guide

### Prerequisites

- Java 17 or higher
- Maven 3.8 or higher
- SQLite (included via JDBC, no installation needed; DB file auto-creates)
- Git (optional for source cloning)
- Internet for dependencies (first run)

### Installation & Running

#### 1. Clone and Enter Project Folder
```bash
git clone https://github.com/imranshiundu/123.git
cd "123"
```

#### 2. Build and Start
```bash
mvn clean spring-boot:run
```

#### 3. Verify Startup
Check the terminal output:
```
Started MoviesApiApplication ...
Tomcat started on port 8081 ...
Database connected: jdbc:sqlite:movies.db
Swagger UI available at /swagger-ui/index.html
```

#### 4. API Documentation
- Access interactive API docs at  
  [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

#### 5. Base API URL
The main API base path: `http://localhost:8081/api/`

---

## Configuring SQLite

- SQLite JDBC (sqlite-jdbc 3.41.2.1) is used.
- DB file: `movies.db` auto-created in project root.
- Main settings in `src/main/resources/application.properties`:
  ```
  spring.datasource.url=jdbc:sqlite:movies.db
  spring.datasource.driver-class-name=org.sqlite.JDBC
  spring.jpa.database-platform=com.zaxxer.hikari.HikariDataSource
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
  ```

---

## Usage Examples

### Create a Movie

```http
POST /api/movies
Content-Type: application/json

{
  "title": "Inception",
  "releaseYear": 2010,
  "duration": 148,
  "genreIds": [1,3],
  "actorIds": [2,8,15]
}
```

### Search Movies by Title

```http
GET /api/movies/search?title=matrix
```

### Get All Movies with Pagination

```http
GET /api/movies?page=0&size=10
```

### Get Movies by Genre or Year

```http
GET /api/movies?genre=1
GET /api/movies?year=2010
```

### Get Movie's Actors

```http
GET /api/movies/1/actors
```

### Update a Movie

```http
PATCH /api/movies/1
Content-Type: application/json

{
  "title": "Inception Extended",
  "duration": 160
}
```

### Delete a Genre (force delete supported)

```http
DELETE /api/genres/1?force=true
```

---

## API Documentation & Testing

- **Swagger UI:** All available endpoints with model schemas and live request/response testing  
  [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

- **Postman Collection:**  
  Import `src/main/java/com/example/moviesapi/postman/MoviesAPI.postman_collection.json`  
  Set baseUrl to `http://localhost:8081/api/`  
  Example requests:
    - Create movie
    - List actors/movies/genres
    - Search/filter/pagination
    - Error cases & validation

---

## Database Design & Relationships

- **Movies, Genres, Actors**:  
  - All entities have immutable auto-generated `id`.
  - Many-to-many relationships:
    - Movie <-> Genre
    - Movie <-> Actor
  - The link tables are managed by JPA/Hibernate in SQLite.

---

## Testing Guide

Testing validates all core and advanced features.
- Use **Swagger UI** for live endpoint tests.
- Use **Postman collection** for automated/iterative testing.

**General Steps:**
1. Download, build, and run the code.
2. Test each endpoint for CRUD, search, filter, pagination, deletion.
3. Validate error handling (404, 400, etc) and input validation (invalid data).
4. Verify relationships and sample data (at least 5 genres, 20 movies, 15 actors).
5. Use force delete to test relationship handling.

_For team testing guidelines:_
- Divide tests among reviewers
- Provide feedback and request fixes as needed
- Clearly state mandatory vs. optional fixes
- Repeat testing after changes

---

## Core Requirements Checklist (with File Locations)

| Requirement | File(s) / Folder(s) | Notes |
|-------------|----------------------|-------|
| RESTful URL design | controller/ | All endpoints use /api/ prefix |
| Four HTTP methods | controller/ | GET, POST, PATCH, DELETE |
| CRUD operations | service/, controller/ | For Movie, Actor, Genre |
| Dependency injection | service/, controller/ | Via constructor/autowired |
| Sample data | resources/data.sql | At least 5 genres, 20 movies, 15 actors |
| Relationship scenarios | model/, data.sql | Many-to-many logic |
| JpaRepository | repository/ | findAll, findById, custom queries |
| @SpringBootApplication | MoviesApiApplication.java | Main entry point |
| @Entity annotation | model/ | All entities annotated |
| Genre fields | Genre.java | id, name |
| Movie fields | Movie.java | id, title, releaseYear, duration |
| Actor fields | Actor.java | id, name, birthDate (YYYY-MM-DD) |
| ManyToMany, JoinTable | model/ | Used between entities |
| Eager vs Lazy loading | model/ | Documented in entity comments |
| Filtering, search | repository/, controller/ | Query params supported |
| Pagination | repository/, controller/ | Pageable supported |
| Validation | dto/, exception/ | @Valid, bean validation, error handler |
| Error handling | exception/ | Custom exceptions, global handler |
| SQLite config | application.properties, pom.xml | Correct JDBC driver set |
| DTOs | dto/ | Clear request/response schemas |
| Status codes | exception/, controller/ | 200, 201, 204, 400, 404, etc. |
| Postman collection | postman/ | For reproducible API tests |
| Documentation | README.md, Swagger UI | Setup & usage guide |
| Clean, commented code | model/, service/, controller/ | JavaDocs, inline comments |

---

## FAQ

**Where is the main API endpoint?**  
- All endpoints start with: `http://localhost:8081/api/`

**Where are entities and their relationships defined?**  
- See `src/main/java/com/example/moviesapi/model/`

**Error handling structure?**  
- See `src/main/java/com/example/moviesapi/exception/GlobalExceptionHandler.java`

**API exploration?**  
- Open [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

**Database config?**  
- See `src/main/resources/application.properties`  
- SQLite JDBC version: 3.41.2.1

**Test API endpoints with sample data?**  
- Use the Postman collection at `src/main/java/com/example/moviesapi/postman/MoviesAPI.postman_collection.json`  
- Or, test directly in Swagger UI.

---

## Contact

For bugs, requests, or questions, create an issue or contact the repo maintainer.

---
**Enjoy using Movies API for learning, testing, or building real-world movie applications with Java, Spring Boot, and SQLite!**