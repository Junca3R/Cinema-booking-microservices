# 🎬 Cinema Booking Microservices

Sistema distribuido y contenedorizado para la gestión de cartelera de cine y reserva de entradas, construido bajo una arquitectura de microservicios totalmente desacoplados, con persistencia políglota y comunicación síncrona entre servicios.

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791?logo=postgresql)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

---

## 📖 Tabla de contenidos

- [Descripción general](#-descripción-general)
- [Arquitectura](#-arquitectura)
- [Stack tecnológico](#-stack-tecnológico)
- [Microservicios](#-microservicios)
  - [ms-auth-users](#1-ms-auth-users-puerto-8085)
  - [ms-cinema-catalog](#2-ms-cinema-catalog-puerto-8086)
  - [ms-bookings](#3-ms-bookings-puerto-8087)
- [Estructura del repositorio](#-estructura-del-repositorio)
- [Flujo de una reserva (end-to-end)](#-flujo-de-una-reserva-end-to-end)
- [Puesta en marcha](#-puesta-en-marcha)
- [Variables de entorno](#-variables-de-entorno)
- [Endpoints principales](#-endpoints-principales)
- [Seguridad](#-seguridad)
- [Roadmap / Mejoras futuras](#-roadmap--mejoras-futuras)
- [Licencia](#-licencia)

---

## 🧩 Descripción general

**Cinema Booking Microservices** simula el backend de una plataforma de venta de entradas de cine. El sistema está dividido en tres dominios de negocio independientes —**usuarios/autenticación**, **catálogo de películas y funciones**, y **reservas**— cada uno con su propia base de datos, su propio ciclo de despliegue y su propio contenedor Docker, comunicándose entre sí mediante **Spring Cloud OpenFeign**.

Este enfoque permite:

- ✅ Escalabilidad independiente por servicio.
- ✅ Aislamiento de fallos (un caído no tumba a los demás).
- ✅ Persistencia políglota (cada servicio usa el motor de base de datos más adecuado a su necesidad).
- ✅ Despliegue reproducible mediante Docker Compose.

---

## 🏗️ Arquitectura

```
                          ┌─────────────────────┐
                          │      Cliente         │
                          │ (Postman / Frontend) │
                          └──────────┬───────────┘
                                     │
              ┌──────────────────────┼───────────────────────┐
              │                      │                       │
     ┌────────▼─────────┐  ┌─────────▼─────────┐  ┌──────────▼─────────┐
     │  ms-auth-users     │  │ ms-cinema-catalog  │  │   ms-bookings       │
     │  Puerto: 8085      │  │ Puerto: 8086        │  │   Puerto: 8087       │
     │  DB: PostgreSQL     │  │ DB: MySQL           │  │   DB: PostgreSQL     │
     │  db_cinema_users    │  │ db_cinema_catalog    │  │   db_cinema_bookings │
     └────────────────────┘  └─────────▲──────────┘  └──────────┬─────────┘
                                        │                         │
                                        └────── Feign Client ─────┘
                                        (CatalogClient - validación de
                                         función y descuento de asientos)
```

- **ms-bookings** actúa como orquestador de la transacción de reserva: valida la función y descuenta asientos disponibles en **ms-cinema-catalog** de forma síncrona mediante un cliente Feign declarativo (`CatalogClient`).
- **ms-auth-users** es responsable exclusivamente de emitir y validar identidad (JWT), desacoplado del resto del dominio de negocio.

---

## 🛠️ Stack tecnológico

| Categoría                  | Tecnología                                   |
|-----------------------------|-----------------------------------------------|
| Lenguaje                    | Java 17+                                       |
| Framework                   | Spring Boot 3.x                                |
| Comunicación entre servicios| Spring Cloud OpenFeign                         |
| Seguridad                   | Spring Security + JWT (JSON Web Tokens)        |
| Persistencia                | Spring Data JPA + Hibernate                    |
| Bases de datos              | PostgreSQL y MySQL (persistencia políglota)    |
| Reducción de boilerplate    | Lombok                                         |
| Gestión de dependencias     | Maven                                          |
| Infraestructura             | Docker + Docker Compose (multi-stage builds)   |

---

## 🧱 Microservicios

### 1. `ms-auth-users` (Puerto 8085)

**Base de datos:** PostgreSQL — `db_cinema_users`

**Responsabilidad:** registro y autenticación de usuarios.

- Contraseñas hasheadas con `BCryptPasswordEncoder`.
- `JwtUtil` genera un token firmado con expiración de **24 horas**, incluyendo en los claims el `email` y el `rol` (`CLIENTE` o `ADMIN`) del usuario.

| Método | Endpoint                     | Descripción                          |
|--------|-------------------------------|---------------------------------------|
| POST   | `/api/v1/auth/register`      | Registro de nuevos usuarios            |
| POST   | `/api/v1/auth/login`         | Login y retorno de token JWT           |

---

### 2. `ms-cinema-catalog` (Puerto 8086)

**Base de datos:** MySQL — `db_cinema_catalog`

**Responsabilidad:** gestión de cartelera, películas, salas y funciones (showtimes).

**Entidades principales:**

- `Movie` → `id`, `title`, `genre`, `durationMinutes`, `rating`
- `Showtime` → `id`, `movie`, `roomName`, `startTime`, `availableSeats`, `ticketPrice`

| Método | Endpoint                                         | Descripción                              |
|--------|----------------------------------------------------|--------------------------------------------|
| POST   | `/api/v1/movies`                                   | Registrar una película                     |
| GET    | `/api/v1/movies`                                   | Listar películas                            |
| POST   | `/api/v1/showtimes`                                | Crear una función                          |
| GET    | `/api/v1/showtimes`                                | Listar funciones                            |
| PUT    | `/api/v1/showtimes/{id}/seats?seats={cantidad}`    | Descontar asientos disponibles              |

---

### 3. `ms-bookings` (Puerto 8087)

**Base de datos:** PostgreSQL — `db_cinema_bookings`

**Responsabilidad:** gestión transaccional de reservas de boletos.

**Entidad principal:**

- `Booking` → `id`, `showtimeId`, `userEmail`, `seatCount`, `totalPrice`, `status`, `bookingDate`

**Integración Feign:** consume `ms-cinema-catalog` mediante `CatalogClient` para:

1. Validar la existencia de la función (`Showtime`).
2. Descontar los asientos disponibles de forma síncrona.
3. Calcular el `totalPrice` multiplicando `seatCount` por el `ticketPrice` obtenido del catálogo.
4. Persistir la reserva con estado `CONFIRMADA`.

| Método | Endpoint                | Descripción                                         |
|--------|---------------------------|--------------------------------------------------------|
| POST   | `/api/v1/bookings`        | Crear una reserva (valida y descuenta asientos)         |
| GET    | `/api/v1/bookings`        | Listar reservas                                          |

---

## 📂 Estructura del repositorio

```
cinema-booking-microservices/
├── ms-auth-users/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── ms-cinema-catalog/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── ms-bookings/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
└── docker-compose.yml
```

---

## 🔄 Flujo de una reserva (end-to-end)

1. El usuario se registra en `ms-auth-users` (`POST /api/v1/auth/register`).
2. El usuario inicia sesión y obtiene un JWT (`POST /api/v1/auth/login`).
3. El usuario consulta la cartelera y las funciones disponibles en `ms-cinema-catalog`.
4. El usuario crea una reserva en `ms-bookings` (`POST /api/v1/bookings`), enviando `showtimeId` y `seatCount`.
5. `ms-bookings` invoca vía Feign a `ms-cinema-catalog` para:
   - Verificar que la función existe y tiene asientos suficientes.
   - Descontar los asientos reservados.
6. `ms-bookings` calcula el precio total y persiste la reserva con estado `CONFIRMADA`.

---

## 🚀 Puesta en marcha

### Requisitos previos

- Docker y Docker Compose instalados.
- (Opcional para desarrollo local) JDK 17+ y Maven.

### Levantar todo el sistema con Docker Compose

```bash
# Desde la raíz del repositorio
docker-compose up --build
```

Esto realizará lo siguiente:

- Levanta un contenedor **MySQL** (puerto host `3308`) y crea la base de datos `db_cinema_catalog`.
- Levanta un contenedor **PostgreSQL** (puerto host `5434`) y crea las bases de datos `db_cinema_users` y `db_cinema_bookings`.
- Compila y construye cada microservicio a partir de sus `Dockerfile` multi-stage (Maven + Alpine).
- Configura las variables de entorno necesarias para que `ms-bookings` resuelva `ms-cinema-catalog` mediante la red interna de Docker (`http://ms-cinema-catalog:8086`), en lugar de `localhost`.

### Puertos expuestos

| Servicio             | Puerto host |
|-----------------------|-------------|
| ms-auth-users          | 8085        |
| ms-cinema-catalog      | 8086        |
| ms-bookings            | 8087        |
| MySQL                  | 3308        |
| PostgreSQL             | 5434        |

---

## ⚙️ Variables de entorno

Cada microservicio se configura mediante variables de entorno inyectadas por `docker-compose.yml`. Ejemplo relevante para `ms-bookings`:

```yaml
environment:
  CATALOG_SERVICE_URL: http://ms-cinema-catalog:8086
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/db_cinema_bookings
```

> ⚠️ En entornos productivos, reemplazar las credenciales de bases de datos y el secreto de firma JWT por variables gestionadas mediante un vault o secret manager (no versionar en el repositorio).

---

## 🔐 Seguridad

- Autenticación basada en **JWT** firmado, con expiración de 24 horas.
- Contraseñas almacenadas con hash **BCrypt** (nunca en texto plano).
- Los claims del token incluyen `email` y `role`, permitiendo control de acceso basado en roles (`CLIENTE` / `ADMIN`) en los distintos microservicios.

---

## 🗺️ Roadmap / Mejoras futuras

- [ ] Migrar la comunicación síncrona (Feign) a mensajería asíncrona (Kafka/RabbitMQ) para desacoplar aún más los servicios.
- [ ] Añadir **Circuit Breaker** (Resilience4j) en las llamadas Feign de `ms-bookings`.
- [ ] Incorporar un **API Gateway** (Spring Cloud Gateway) como punto de entrada único.
- [ ] Registrar servicios en un **Service Discovery** (Eureka / Consul).
- [ ] Añadir tests de integración con Testcontainers.
- [ ] Documentar la API con OpenAPI/Swagger en cada microservicio.
- [ ] Añadir manejo centralizado de errores y trazabilidad distribuida (Sleuth/Zipkin o OpenTelemetry).

---

## 📄 Licencia

Este proyecto se distribuye bajo la licencia **MIT**. Consulta el archivo `LICENSE` para más detalles.
