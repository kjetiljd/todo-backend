# Todo Backend API

Spring Boot REST API for the todo list demo application.

## Features

- Full CRUD operations for todos
- SQLite database with Flyway migrations
- Integration with sorting service
- CORS support for frontend
- Actuator endpoints for monitoring

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- SQLite with Hibernate
- Flyway for migrations
- WebFlux for HTTP client

## Development

```bash
# Build the application
./gradlew build

# Run the application (http://localhost:8080)
./gradlew bootRun

# Run tests
./gradlew test

# Clean build artifacts
./gradlew clean
```

## Database

The application uses SQLite with the database file stored at `./data/todos.db`. Flyway handles schema migrations automatically.

## API Endpoints

- `GET /api/todos?sortBy={sortBy}` - Get all todos (optionally sorted)
- `GET /api/todos/{id}` - Get todo by ID
- `POST /api/todos` - Create new todo
- `PUT /api/todos/{id}` - Update todo
- `PATCH /api/todos/{id}/toggle` - Toggle completion status
- `DELETE /api/todos/{id}` - Delete todo
- `GET /api/todos/count` - Get total count

### Sorting Options

- `createdAt` - By creation date (default)
- `priority` - By priority (high → medium → low)
- `dueDate` - By due date
- `alphabetical` - By title
- `completion` - By completion status

## External Dependencies

- **Sorting Service** (http://localhost:3001) - For advanced sorting functionality
- Falls back to local sorting if service unavailable

## Configuration

Environment variables:
- `SORTER_SERVICE_URL` - URL of the sorting service (default: http://localhost:3001)