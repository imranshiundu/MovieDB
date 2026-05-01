# MovieDB

MovieDB is a Spring Boot + SQLite movie database platform with a Netlify-safe static dashboard. It includes a REST API, browser UI, Swagger documentation, seeded movie data, search, filtering, pagination, actor and genre relationships, rich movie metadata, and database summary endpoints.

The project now works in two modes:

1. **Live backend mode** — run the Spring Boot app and use the real SQLite database/API.
2. **Netlify/static mode** — deploy the dashboard to Netlify and show demo movie records even when the Java backend is not running.

Netlify cannot run the Java Spring Boot backend or SQLite database. It only serves the frontend. For the full live database, deploy the backend separately on a Java-friendly host such as Render, Railway, Fly.io, a VPS, or any server that can run Maven/Java.

## What is included

- Netlify-ready dashboard UI
- Static demo fallback data for frontend previews
- REST API under `/api`
- Swagger/OpenAPI documentation at `/swagger-ui.html`
- SQLite database storage
- Seeded database with classics, modern films, international films, and African cinema examples
- Rich movie fields: director, language, country, IMDb rating, MPAA rating, poster URL, and overview
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
- Indexes for faster title, year, director, country, rating, actor, genre, and relationship lookups

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
| Frontend | Static HTML, CSS, JavaScript |
| Static deployment | Netlify |
| Build | Maven |

## Project structure

```text
MovieDB/
├── pom.xml
├── netlify.toml
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
        ├── data-enrichment.sql
        └── static/index.html
```

## Quick start: live backend mode

```bash
git clone https://github.com/imranshiundu/MovieDB.git
cd MovieDB
mvn clean spring-boot:run
```

Open:

- Dashboard: `http://localhost:8081/`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- API status: `http://localhost:8081/api/status`
- Database summary: `http://localhost:8081/api/database/summary`

## Netlify deployment

This repo includes `netlify.toml`:

```toml
[build]
  publish = "src/main/resources/static"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

On Netlify:

- Build command: leave empty
- Publish directory: `src/main/resources/static`

The Netlify version will show demo records because Netlify cannot run the Spring Boot API. To connect Netlify to a real backend later, deploy the Java app elsewhere and update the dashboard fetch base URL to that deployed API.

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

## Example movie payload

```http
POST /api/movies/with-dto
Content-Type: application/json

{
  "title": "Nairobi Half Life",
  "releaseYear": 2012,
  "duration": 96,
  "director": "David Tosh Gitonga",
  "language": "Swahili, English",
  "country": "Kenya",
  "imdbRating": 7.3,
  "mpaaRating": "NR",
  "overview": "A young actor from rural Kenya is pulled into Nairobi’s criminal underworld while chasing his dream.",
  "genreIds": [2, 10],
  "actorIds": [37]
}
```

## Database improvements

The database now has two seed layers:

- `data.sql` — original movies, actors, genres, and relationships
- `data-enrichment.sql` — richer metadata, extra films, extra actors, extra genres, and performance indexes

Added enrichment includes:

- Director names
- Languages
- Countries
- IMDb ratings
- MPAA/age ratings
- Plot overviews
- International cinema examples
- African cinema examples
- Search/index improvements

## Dashboard UI

The dashboard is served from `src/main/resources/static/index.html` and loads live data from:

- `/api/movies/paged?size=100`
- `/api/genres`
- `/api/actors`

When these endpoints are unavailable, such as on Netlify, it switches to demo mode automatically.

It provides:

- movie count, actor count, and genre count
- search by title, director, country, language, or overview
- year filter
- sorting by top rated, newest, oldest, A-Z, and duration
- rich movie cards with rating, director, country, and overview

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
