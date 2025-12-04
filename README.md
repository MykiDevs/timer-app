# Spring Boot Timer API (JWT + RBAC)

A robust backend service for managing productivity timers and user profiles. Features secure JWT authentication, role-based access control (RBAC), and avatar file management.

## Tech Stack

- **Java 17**
- **Spring Boot 3** (Web, Security, Data JPA)
- **PostgreSQL / H2** (Persistence)
- **Spring Security** (Stateless JWT Auth)
- **Apache Tika** (MIME Type Validation)
- **MapStruct** (Zero-overhead DTO Mapping)
- **OpenAPI / Swagger** (Documentation)

## Features

- **Authentication:** Full flow including Signup, Login, and Refresh Token rotation.
- **Timer Engine:** State machine for timers (Created -> Running -> Paused -> Finished) with duration calculation.
- **Role-Based Access:** 
  - `ROLE_USER`: Manage own timers and profile.
  - `ROLE_ADMIN`: View and manage all users and timers system-wide.
- **File Uploads:** Secure avatar upload with MIME type detection and strict size limits.
- **Pagination:** Global pagination support for list endpoints.

## Configuration

### Prerequisites
- Java 17+
- Docker (optional, for DB)

### Environment Variables
Configure these in `application.properties` or your environment:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/timer_db
spring.datasource.username=postgres
spring.datasource.password=password

# JWT Settings
security.jwt.secret-key=YOUR_BASE64_ENCODED_SECRET_KEY_HERE
security.jwt.access-token-expiration-time=3600000  # 1 hour
security.jwt.refresh-token-expiration-time=86400000 # 1 day

# File Storage
upload.path=./uploads
```

### Running the App

```bash
# Run tests
./gradlew test

# Start server
./gradlew bootRun
```

Server starts at `http://localhost:8080`.
Swagger UI available at `http://localhost:8080/swagger-ui.html`.

## API Reference

### Auth
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/signup` | Register new user |
| `POST` | `/api/auth/login` | Get Access & Refresh tokens |
| `POST` | `/api/auth/refresh` | Rotate access token |

### Timers
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/timers` | Create a timer |
| `GET` | `/api/timers` | List my timers |
| `PATCH` | `/api/timers/{uuid}/start` | Start timer |
| `PATCH` | `/api/timers/{uuid}/pause` | Pause timer |
| `PATCH` | `/api/timers/{uuid}/resume` | Resume timer |
| `PATCH` | `/api/timers/{uuid}/finish` | Force finish |

### Users
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/users/profile` | Get my profile |
| `POST` | `/api/users/profile/avatar` | Upload avatar (JPG/PNG) |
| `PATCH` | `/api/users/profile` | Update password/email |

## Architecture Highlights

- **DTO Projection:** Uses JSON Views (`Views.Public`, `Views.User`, `Views.Admin`) to strictly control data exposure per endpoint without creating redundant DTO classes.
- **Exception Handling:** Centralized `GlobalApiExceptionHandler` transforms business exceptions into standardized JSON error responses.
- **Cleanup Job:** Includes a background scheduler (`TimerCleanupService`) to manage stale timer data.

## Testing

Integration tests use `MockMvc` with a mock database environment.

```bash
./gradlew test
```

## Roadmap

- [ ] **Cloud Storage:** Migrate `LocalStorageService` to AWS S3.
- [ ] **Soft Delete:** Replace hard deletion of timers with an archiving strategy.
- [ ] **WebSocket:** Add real-time timer sync across devices.
