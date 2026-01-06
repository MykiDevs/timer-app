# Spring Boot Timer API

A robust backend service for managing productivity timers and user profiles. Features secure JWT authentication, role-based access control (RBAC), and avatar file management.

## Tech Stack

- **Java 17**
- **Spring Boot 3**
- **PostgreSQL / H2**
- **Spring Security**
- **Apache Tika**
- **MapStruct**
- **OpenAPI / Swagger**

## Getting Started

### Prerequisites
- Java 17+
- PostgreSQL

### Installation and Run
1. Clone the repository:
```bash
git clone https://github.com/MykiDevs/timer-app.git
cd timer-app
```
2. Build:
```bash
./mwnw clean install
```
3. Run:
```bash
./mwnw spring-boot:run
```
Server starts at `http://localhost:8080`.

## API Reference
For full API Documentation please check Swagger  `http://localhost:8080/swagger-ui/index.html`.

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

## License
[MIT](https://github.com/MykiDevs/timer-app/blob/main/LICENSE)
