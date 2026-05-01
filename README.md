# MovieDB

MovieDB is a polished Spring Boot + SQLite movie database platform. It includes a REST API, built-in browser dashboard, Swagger documentation, seeded movie data, search, filtering, pagination, actor and genre relationships, and database summary endpoints.

This project is no longer just a class-style CRUD API. It is a small local-first movie database system that can be used for learning, demos, portfolio work, API testing, or as a backend foundation for a larger movie catalogue product.

## What is included

- Built-in dashboard UI at `/`
- REST API under `/api`
- Swagger/OpenAPI documentation at `/swagger-ui.html`
- SQLite database storage
- Seeded demo data for movies, actors, and genres
- Movie CRUD operations
- Actor CRUD operations
- Genre CRUD operations
- Search by movie title, actor name, and genre name
- Pagination support
- Filtering by year, actor, genre, and duration
- Many-to-many relationships between movies, actors, and genres
- Statistics endpoints
- Database summary endpoint
- Environment-based configuration
- Demo/public API key documentation for local integrations

## Tech stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.0 |
| API | Spring Web REST controllers |
| Database | SQLite |
| ORM | Spring Data JPA + Hibernate |
| Validation | Jakarta Bean Validation |
| Docs | Springdoc OpenAPI / Swagger UI |
| Frontend | Static HTML, CSS, JavaScript served by Spring Boot |
| Build | Maven |

## Project structure

```text
MovieDB/
├── pom.xml
├── env.example
├── README.md
└── src/main/
    ├── java/com/example/moviesapi/
    │   ├── MoviesApiApplication.java
    │   ├── config/
    │   │   └── OpenApiConfig.java
    │   ├── controller/
    │   │   ├── HomeController.java
    │   │   ├── DatabaseController.java
    │   │   ├── MovieController.java
    │   │   ├── ActorController.java
    │   │   └── GenreController.java
    │   ├── dto/
    │   ├── exception/
    │   ├── model/
    │   ├── repository/
    │   └── service/
    └── resources/
        ├── application.properties
        ├── data.sql
        └── static/index.html
```

## Quick start

### 1. Clone

```bash
git clone https://github.com/imranshiundu/MovieDB.git
cd MovieDB
```

### 2. Run

```bash
mvn clean spring-boot:run
```

### 3. Open the app

- Dashboard: `http://localhost:8081/`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- API status: `http://localhost:8081/api/status`
- Database summary: `http://localhost:8081/api/database/summary`

## Environment variables

Copy the example file when you need local overrides:

```bash
cp env.example .env
```

Main settings:

| Variable | Default | Purpose |
|---|---:|---|
| `PORT` | `8081` | Server port |
| `MOVIEDB_DATABASE_URL` | `jdbc:sqlite:movies.db` | SQLite database file |
| `MOVIEDB_DDL_AUTO` | `create` | Schema strategy for local demos |
| `MOVIEDB_SQL_INIT` | `always` | Loads demo data on startup |
| `MOVIEDB_SHOW_SQL` | `false` | SQL console logging |
| `MOVIEDB_ALLOWED_ORIGINS` | `*` | CORS setting |
| `MOVIEDB_PUBLIC_API_KEYS` | `moviedb-demo-key,moviedb-readonly-key` | Demo key list for integrations/documentation |
| `OMDB_API_KEY` | `demo` | Optional external movie provider key |
| `TMDB_API_KEY` | empty | Optional external movie provider key |

Important: the included demo keys are not production secrets. They are public placeholders for local testing and documentation. For real deployment, use private environment variables and never commit real API keys.

## Core endpoints

### System

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/` | Dashboard UI |
| GET | `/dashboard` | Dashboard UI |
| GET | `/api/status` | API health/status metadata |
| GET | `/api/database/summary` | Record counts and database summary |
| GET | `/swagger-ui.html` | Interactive API documentation |

### Movies

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/movies` | List all movies |
| GET | `/api/movies/paged?page=0&size=10` | Paginated movies |
| GET | `/api/movies/{id}` | Get one movie |
| POST | `/api/movies` | Create movie |
| POST | `/api/movies/with-dto` | Create movie with DTO body |
| PATCH | `/api/movies/{id}` | Partially update movie |
| DELETE | `/api/movies/{id}?force=true` | Delete movie |
| GET | `/api/movies/search?title=matrix` | Search by title |
| GET | `/api/movies/by-year/{year}` | Filter by year |
| GET | `/api/movies/by-genre/{genreId}` | Filter by genre |
| GET | `/api/movies/by-actor/{actorId}` | Filter by actor |
| GET | `/api/movies/advanced-search` | Search by title, year range, and duration range |
| GET | `/api/movies/{id}/actors` | Actors attached to movie |
| GET | `/api/movies/{id}/genres` | Genres attached to movie |
| PATCH | `/api/movies/{id}/relationships` | Replace movie relationships |

### Actors

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/actors` | List actors |
| GET | `/api/actors/paged?page=0&size=10` | Paginated actors |
| GET | `/api/actors/{id}` | Get one actor |
| POST | `/api/actors` | Create actor |
| PATCH | `/api/actors/{id}` | Update actor |
| DELETE | `/api/actors/{id}?force=true` | Delete actor |
| GET | `/api/actors/search?name=tom` | Search actors |
| GET | `/api/actors/{id}/movies` | Movies for actor |

### Genres

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/genres` | List genres |
| GET | `/api/genres/paged?page=0&size=10` | Paginated genres |
| GET | `/api/genres/{id}` | Get one genre |
| POST | `/api/genres` | Create genre |
| PATCH | `/api/genres/{id}` | Update genre |
| DELETE | `/api/genres/{id}?force=true` | Delete genre |
| GET | `/api/genres/search?name=action` | Search genres |
| GET | `/api/genres/{id}/movies` | Movies for genre |
| POST | `/api/genres/bulk` | Bulk-create genres |

## Example requests

### Create a movie with relationships

```http
POST /api/movies/with-dto
Content-Type: application/json

{
  "title": "Inception",
  "releaseYear": 2010,
  "duration": 148,
  "genreIds": [1, 4],
  "actorIds": [3]
}
```

### Advanced search

```http
GET /api/movies/advanced-search?title=the&minYear=2000&maxYear=2024&minDuration=90&maxDuration=180&page=0&size=10
```

### Database summary

```http
GET /api/database/summary
```

Example response:

```json
{
  "database": "SQLite",
  "movies": 56,
  "actors": 30,
  "genres": 18,
  "dashboard": "/",
  "swagger": "/swagger-ui.html"
}
```

## Dashboard UI

The dashboard is served from `src/main/resources/static/index.html` and loads live data from:

- `/api/movies/paged?size=100`
- `/api/genres`
- `/api/actors`

It provides:

- movie count, actor count, and genre count
- title search
- year filter
- sorting by newest, oldest, A-Z, and duration
- direct JSON links for each movie

## Data model

MovieDB uses three main entities:

- `Movie`: title, release year, duration
- `Actor`: name, birth date
- `Genre`: name

Relationships:

- Movie to Actor: many-to-many
- Movie to Genre: many-to-many

The seeded database includes classics, modern blockbusters, actors, genres, and relationship mappings.

## Development notes

For local demo work, the default schema strategy is `create`, so the database is rebuilt and reseeded on startup. For persistent local data, run with:

```bash
MOVIEDB_DDL_AUTO=update MOVIEDB_SQL_INIT=never mvn spring-boot:run
```

For production, move from SQLite to PostgreSQL or MySQL, disable public CORS, use real authentication, and keep API keys in environment variables.

## Upgrade ideas still available

- Add real API-key middleware for protected write endpoints
- Add PostgreSQL profile for deployment
- Add Dockerfile and docker-compose
- Add tests for controllers and services
- Add import endpoint from OMDb/TMDb
- Add posters, ratings, reviews, watchlists, and user accounts
- Add CI with Maven test/build checks

## Maintainer

Built and maintained by Imran Shiundu.

Portfolio: `https://imranisdev.top`
