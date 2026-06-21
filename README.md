# 🔗 Spring Boot Microservices Intercommunication

A hands-on Spring Boot microservices project demonstrating **service discovery** (Netflix Eureka) and **synchronous inter-service communication** using a load-balanced `RestTemplate`. The system consists of three independent services — a Eureka **Registry**, an **MsTesting** service, and a **Second** service backed by MongoDB — that register themselves and talk to each other purely by application name, with no hardcoded hosts or ports.

![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.0-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Eureka](https://img.shields.io/badge/Netflix-Eureka-1DB954?style=for-the-badge&logo=netflix&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)

---

## 📐 Architecture

```
                          ┌─────────────────────────┐
                          │   registry (Eureka)      │
                          │   localhost:8761          │
                          │   Service Registry         │
                          └─────────────▲─────────────┘
                                        │ register / discover
                       ┌────────────────┴────────────────┐
                       │                                  │
          ┌────────────┴────────────┐        ┌────────────┴────────────┐
          │   mstesting service       │        │   second service          │
          │   localhost:8081          │◄───────│   localhost:8082          │
          │   GET /id                 │ Load-   │   GET /{id}               │
          │   GET /name                │ balanced│  POST /post              │
          └────────────────────────────┘ RestTemplate │  GET /getusername    │
                                                        └────────────┬────────┘
                                                                     │
                                                          ┌──────────▼──────────┐
                                                          │   MongoDB              │
                                                          │   localhost:27017       │
                                                          │   db: second             │
                                                          └─────────────────────────┘
```

**Flow:** `second` calls `GET http://mstesting/name` through a `@LoadBalanced RestTemplate`. Spring Cloud LoadBalancer resolves the logical name `mstesting` to a live instance address by querying the `registry` (Eureka Server), so no service ever needs to know another service's actual host or port.

---

## 🧩 Services

| Service | Port | Role | Key Dependencies |
|---|---|---|---|
| **`registry`** | `8761` | Eureka Service Registry / Discovery Server | `spring-cloud-starter-netflix-eureka-server` |
| **`MsTesting`** | `8081` | Eureka client exposing simple identity endpoints, called by `second` | `spring-cloud-starter-netflix-eureka-client` |
| **`second`** | `8082` | Eureka client with a MongoDB-backed CRUD API; calls `MsTesting` via service discovery | `spring-cloud-starter-netflix-eureka-client`, `spring-boot-starter-data-mongodb` |

---

## 🛠️ Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.5.3
- **Cloud:** Spring Cloud 2025.0.0 (Netflix Eureka Server & Client)
- **Database:** MongoDB (used by the `second` service)
- **HTTP Client:** `RestTemplate` with `@LoadBalanced` for client-side load balancing
- **Build Tool:** Gradle (each module ships its own Gradle wrapper)
- **Testing:** Spring Boot Starter Test / JUnit Platform

---

## 📂 Project Structure

```
springboot_microservices_intercommunication/
├── registry/                  # Eureka Server
│   └── src/main/java/com/example/registry/
│       └── RegistryApplication.java
│
├── MsTesting/                  # Eureka Client - identity service
│   └── src/main/java/com/example/MsTesting/
│       ├── mstestingApplication.java
│       ├── Controller.java
│       └── ServiceLayer.java
│
├── second/                     # Eureka Client - MongoDB-backed service
│   └── src/main/java/com/example/second/
│       ├── SecondApplication.java
│       ├── Controller.java
│       ├── Serv.java
│       ├── Repo.java
│       ├── Entity.java
│       └── Config.java         # @LoadBalanced RestTemplate bean
│
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- **JDK 21+**
- **MongoDB** running locally on `27017` (required for the `second` service)
- No need to install Gradle — each service includes its own wrapper (`./gradlew`)

### 1. Clone the repository

```bash
git clone https://github.com/Dhanushvardan/springboot_microservices_intercommunication.git
cd springboot_microservices_intercommunication
```

### 2. Start MongoDB

```bash
mongod --dbpath <your-data-directory>
```

### 3. Start the services — in this order

> ⚠️ The Eureka registry must be up first, so the other services can register against it.

**a. Registry (Eureka Server)**

```bash
cd registry
./gradlew bootRun
```
Eureka dashboard: [http://localhost:8761](http://localhost:8761)

**b. MsTesting**

```bash
cd MsTesting
./gradlew bootRun
```
Runs on `http://localhost:8081`

**c. Second**

```bash
cd second
./gradlew bootRun
```
Runs on `http://localhost:8082`

Once all three are up, refresh the Eureka dashboard — you should see `MSTESTING` and `SECOND` registered as `UP`.

---

## 📡 API Reference

### `MsTesting` service — `http://localhost:8081`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/id` | Returns a static sample ID (`123`) |
| `GET` | `/name` | Returns a static username (`Dhanush`) |

### `second` service — `http://localhost:8082`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/{id}` | Fetch an `Entity` by ID from MongoDB |
| `POST` | `/post` | Save a new `Entity` to MongoDB |
| `GET` | `/getusername` | Calls `MsTesting`'s `/name` endpoint via Eureka + load-balanced `RestTemplate` and returns the result |

**Example — verify inter-service communication:**

```bash
curl http://localhost:8082/getusername
# → "Dhanush"  (fetched live from the MsTesting service via Eureka)
```

**Example — save and fetch an entity:**

```bash
curl -X POST http://localhost:8082/post \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "name": "Dhanush", "designation": "Full Stack Developer"}'

curl http://localhost:8082/1
```

---

## 💡 What This Project Demonstrates

- Registering multiple Spring Boot apps with a **Eureka Server**
- Enabling **service discovery** via `spring-cloud-starter-netflix-eureka-client`
- Performing **synchronous, name-based inter-service calls** with a `@LoadBalanced RestTemplate` instead of hardcoded URLs
- Wiring a Spring Boot service to **MongoDB** with `spring-boot-starter-data-mongodb` and `MongoRepository`
- A minimal, from-scratch microservices skeleton useful as a reference/starting point for larger systems (API Gateway, Config Server, Circuit Breaker, etc. can be layered on top)

---

## 🔭 Possible Next Steps

- [ ] Add an API Gateway (Spring Cloud Gateway) as a single entry point
- [ ] Replace `RestTemplate` with `OpenFeign` for declarative client calls
- [ ] Add Resilience4j for circuit breaking / fallback handling
- [ ] Containerize each service with Docker and add a `docker-compose.yml`
- [ ] Add a centralized Config Server

---

## 👤 Author

**Dhanushvardan**
Full Stack & GenAI Developer
[GitHub](https://github.com/Dhanushvardan) · [LinkedIn](https://linkedin.com/in/dhanushvardan-a-v-j-b238a3201)
