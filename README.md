# Bibliotech - Sistema de Gestión de Biblioteca

[![Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue)](https://maven.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)](https://docs.docker.com/compose/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Sistema full-stack para la administración de documentos bibliográficos: libros, ponencias y artículos científicos. Permite registrar usuarios, crear documentos, reservarlos y llevar un historial completo de eventos.

---

## Stack Tecnológico

| Capa | Tecnología |
|---|---|
| **Backend** | Java 21, Maven, Jakarta Servlets 4.0, Tomcat 9 |
| **Persistencia** | PostgreSQL 16, HikariCP (connection pooling) |
| **Seguridad** | JWT (jjwt 0.11.5), BCrypt (jbcrypt 0.4) |
| **Logging** | SLF4J + Logback (rotación diaria, nivel configurable) |
| **JSON** | Jackson 2.x |
| **Testing** | JUnit 5, Mockito 5, H2 Database |
| **Frontend** | Vanilla JS, Vite, nginx |
| **DevOps** | Docker, Docker Compose, multi-stage builds |

---

## Estructura del Proyecto

```
bibliotech/
├── backend/                    # API REST (Java + Maven + Servlets)
│   ├── Dockerfile              #   Build multi-stage (Maven → Tomcat 9)
│   ├── .dockerignore
│   └── Libreria/
│       ├── pom.xml
│       └── src/
│           ├── main/java/
│           │   ├── config/         # AppConfig (carga properties + env vars)
│           │   ├── controlador/    # FachadaSistema, GestorUsuarios, GestorDocumentos, GestorReservas
│           │   ├── modelo/         # Entidades, DTOs, DAOs, BusinessException
│           │   ├── servlets/       # Endpoints HTTP + JWT Filter
│           │   └── util/           # Validador (input validation)
│           ├── main/resources/     # application.properties, logback.xml
│           └── test/java/          # Tests unitarios (JUnit 5 + Mockito)
├── frontend/                   # SPA estática (Vanilla JS + Vite + nginx)
│   ├── Dockerfile              #   Build multi-stage (Node → nginx)
│   ├── nginx.conf              #   Proxy reverso /api → backend
│   ├── vite.config.js
│   └── src/
│       ├── index.html
│       ├── pages/              #   Vistas HTML
│       ├── js/                 #   Lógica + servicios
│       └── css/                #   Estilos (base.css con variables CSS)
├── db/
│   └── init.sql                # Esquema inicial de la base de datos
├── docker-compose.yml          # Orquestación (db + backend + frontend)
├── .env.example                # Plantilla de variables de entorno
└── README.md
```

---

## Arquitectura del Backend

```
HTTP Request → Servlet → FachadaSistema (Facade)
                              ├── GestorUsuarios
                              ├── GestorDocumentos
                              └── GestorReservas
                                     ↓
                              DAO Layer (JDBC + HikariCP)
                                     ↓
                              PostgreSQL
```

- **FachadaSistema**: Singleton enum que centraliza toda la lógica de negocio
- **Patrones**: Facade, Factory (DocumentoFactory + FabricaDAO), Builder (DTOs), DAO genérico
- **Manejo de errores**: `BusinessException` → `handleError()` en `BaseServlet` → `ErrorResponse` JSON
- **Seguridad**: `JwtFiltro` protege todos los endpoints salvo login/registro
- **Soft delete**: Los documentos se marcan como "Eliminado" en lugar de borrarse
- **Auditoría**: Cada acción queda registrada en la tabla `evento`

---

## API Endpoints

### Autenticación

| Método | Ruta | Auth | Descripción |
|---|---|---|---|
| `POST` | `/usuario/registrar` | No | Registro de nuevo usuario |
| `POST` | `/usuario/login` | No | Login, retorna JWT |

### Usuario (requiere JWT)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/usuario/datos` | Datos del usuario autenticado |
| `GET` | `/usuario/documentos` | Lista documentos del usuario |
| `GET` | `/usuario/reservas` | Lista reservas del usuario |
| `POST` | `/usuario/consultar` | Consultar datos de otro usuario |

### Documentos (requiere JWT)

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/documento/crear` | Crear documento |
| `POST` | `/documento/modificar` | Modificar documento existente |
| `POST` | `/documento/reservar` | Reservar un documento |
| `POST` | `/documento/entregar` | Marcar reserva como entregada |
| `POST` | `/documento/eliminar` | Soft-delete de documento |
| `POST` | `/documento/habilitar` | Reactivar documento eliminado |
| `POST` | `/documento` | Obtener documento por ID |
| `POST` | `/documento/eventos` | Historial de eventos del documento |
| `POST` | `/documento/titulo` | Búsqueda de documentos por título |

---

## Configuración y Despliegue

### Requisitos previos

- [Docker](https://docs.docker.com/get-docker/) y Docker Compose
- [Java 21+](https://adoptium.net/) y [Maven 3.x](https://maven.apache.org/) (solo para desarrollo local)
- [Node.js 22+](https://nodejs.org/) (solo para desarrollo del frontend)

### Despliegue con Docker (recomendado)

```bash
# 1. Clonar el repositorio
git clone <repo-url>
cd bibliotech

# 2. Configurar variables de entorno
cp .env.example .env
# Editar .env con tus valores reales (contraseñas, JWT secret, etc.)

# 3. Levantar los servicios
docker compose up -d

# 4. Acceder a la aplicación
# Frontend: http://localhost:3000
# Backend:  http://localhost:8080/Libreria/
```

### Desarrollo local

```bash
# --- Backend ---
cd backend/Libreria
mvn clean package
# Desplegar el .war generado en Tomcat o ejecutar desde IDE

# --- Frontend ---
cd frontend
cp ../.env.example .env   # o configura las variables manualmente
npm install
npm run dev                # http://localhost:5173 con proxy /api → backend
```

---

## Variables de Entorno

| Variable | Descripción | Default |
|---|---|---|
| `DB_URL` | JDBC URL de PostgreSQL | `jdbc:postgresql://db:5432/LibreriaDB` |
| `DB_USER` | Usuario de la base de datos | `postgres` |
| `DB_PASSWORD` | Contraseña de la base de datos | **Requerido** |
| `DB_MAX_POOL_SIZE` | Tamaño máximo del pool HikariCP | `10` |
| `DB_MIN_IDLE` | Conexiones mínimas inactivas | `2` |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT | **Requerido** |
| `CORS_ALLOWED_ORIGINS` | Origen permitido para CORS | `http://localhost:3000` |
| `LOG_LEVEL` | Nivel de logging (TRACE/DEBUG/INFO/WARN/ERROR) | `INFO` |
| `FRONTEND_PORT` | Puerto del contenedor frontend | `3000` |

> **Importante**: Copia `.env.example` a `.env` y configura `DB_PASSWORD` y `JWT_SECRET` antes de desplegar. El archivo `.env` está en `.gitignore` y nunca debe comitearse.

---

## Testing

```bash
cd backend/Libreria
mvn test
```

Incluye tests unitarios para los tres controladores de negocio y la fábrica de documentos, usando JUnit 5 + Mockito + H2 en memoria.

---

## Base de Datos

El script `db/init.sql` crea automáticamente las tablas al iniciar el contenedor de PostgreSQL:

```
usuario ──┬── documento ──┬── libro
          │               ├── ponencia
          │               ├── articulo
          │               ├── evento
          │               └── reserva
```

---

## Licencia

MIT.
