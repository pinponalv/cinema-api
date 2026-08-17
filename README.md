# 🎬 Cinema API

API REST para la gestión de un cine, incluyendo películas, usuarios, autenticación y control de permisos basado en roles.

**Versión:** 0.0.1-SNAPSHOT | **Java:** 17+ | **Spring Boot:** 4.0.7

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Arquitectura](#️-arquitectura)
- [Instalación](#-instalación)
- [Probar la API con Swagger](#-probar-la-api-con-swagger)
- [Tareas Pendientes](#-tareas-pendientes)
- [Referencias](#-referencias)
- [Licencia](#-licencia)

---

## 📖 Descripción

Cinema API expone operaciones CRUD sobre películas, usuarios, roles y permisos, con autenticación basada en JWT y autorización por rol (RBAC).

- ✅ **Autenticación JWT** - Access token (30 min) + Refresh token (7 días) para renovar la sesión sin volver a loguearse
- ✅ **Control de Acceso basado en Roles (RBAC)** - Usuarios, roles y permisos
- ✅ **Gestión de Películas** - CRUD completo, con búsqueda por título y por género
- ✅ **Upload de Imágenes (Cloudinary)** - Subida del poster de cada película
- ✅ **Spring Security** - Configuración de seguridad avanzada
- ✅ **OpenAPI/Swagger** - Documentación interactiva de API
- ✅ **MySQL Database** - Persistencia relacional
- ✅ **MapStruct** - Mapeo limpio entre DTOs y entidades
- ✅ **Manejo global de errores** - `GlobalExceptionHandler` con respuestas consistentes
- ✅ **Paginación** - En listados de películas y usuarios
- ✅ **Unit Tests** - Cobertura de pruebas para servicios (Movie, Permission, Role, User)
- ⏳ **Docker Support** - Containerización (pendiente)

---

## 🏗️ Arquitectura

La aplicación sigue una arquitectura en capas clásica de Spring Boot:

```
Controller  →  Service (interface)  →  ServiceImpl  →  Repository  →  Entity
   ↓                                                                     ↑
  DTO  ←──────────────────────  Mapper (MapStruct)  ──────────────────┘
```

- **Controller**: recibe el request, valida el body (`@Valid`) y delega en el service.
- **Service (interface)**: define el contrato (`IUserService`, `IMoviesService`, etc.).
- **ServiceImpl**: contiene la lógica de negocio real.
- **Repository**: acceso a datos vía Spring Data JPA.
- **Mapper**: convierte `Entity ↔ DTO` con MapStruct, sin exponer entidades JPA en las respuestas.

Cualquier excepción de negocio lanzada desde una capa (`ResourceNotFoundException`, `DuplicateResourceException`, `InvalidRequestException`, etc.) es interceptada de forma centralizada por `GlobalExceptionHandler`, que arma una respuesta `ApiError` consistente en toda la API.

```
src/main/java/com/example/cinema_api/
├── controller/              # REST endpoints
│   ├── AuthController       # Autenticación (login, registro)
│   ├── UserController       # Gestión de usuarios
│   ├── MovieController      # CRUD de películas
│   ├── RoleController       # Gestión de roles
│   └── PermissionController # Gestión de permisos
│
├── service/                 # Lógica de negocio
│   ├── impl/                # Implementaciones
│   └── I*Service.java       # Contratos de servicio
│
├── repository/              # Acceso a datos (JPA)
│
├── entity/                  # Modelos de base de datos
│   ├── UserSec              # Entidad de usuario
│   ├── Movies                # Entidad de película
│   ├── Roles                 # Entidad de rol
│   └── Permission            # Entidad de permiso
│
├── dto/                     # Transfer Objects (Request/Response)
│
├── mapper/                  # Mapeo entre entidades y DTOs (MapStruct)
│
├── exception/                # Excepciones de negocio + GlobalExceptionHandler + ApiError
│
├── security/                 # JWT, filtros y configuración de Spring Security
│
├── config/                   # Configuración (OpenAPI/Swagger, etc.)
│
└── CinemaApiApplication.java # Clase principal
```

---

## 🚀 Instalación

### Requisitos

- **Java 17** o superior
- **Maven 3.6** o superior
- **MySQL 8.0** o superior
- **Git**

```bash
java -version
mvn -version
mysql --version
```

### 1. Clonar el Repositorio
```bash
git clone <tu-repo-url>
cd cinema-api
```

### 2. Configurar la Base de Datos
```sql
CREATE DATABASE `cinema-api`;
```

### 3. Configurar credenciales

`application.properties` no tiene credenciales hardcodeadas — las lee desde **variables de entorno**. Necesitás definir las siguientes antes de correr la aplicación:

| Variable | Descripción | Ejemplo |
|---|---|---|
| `BD_URL` | URL de conexión a MySQL | `jdbc:mysql://localhost:3306/cinema-api` |
| `BD_USER` | Usuario de MySQL | `root` |
| `BD_PASSWORD` | Contraseña de MySQL | `tu_contraseña` |
| `PRIVATE_KEY` | Clave secreta para firmar los JWT (cualquier string largo random) | `openssl rand -hex 32` |
| `USER_GENERATOR` | Nombre del issuer del JWT (puede ser cualquier valor) | `cinema-api` |
| `CLOUD_NAME` | Cloud name de [Cloudinary](https://cloudinary.com/users/register/free) (cuenta gratuita) | - |
| `CLOUD_KEY` | API key de Cloudinary | - |
| `CLOUD_SECRET` | API secret de Cloudinary | - |

**Opción A — exportarlas en tu shell:**
```bash
export BD_URL="jdbc:mysql://localhost:3306/cinema-api"
export BD_USER="root"
export BD_PASSWORD="tu_contraseña"
export PRIVATE_KEY="$(openssl rand -hex 32)"
export USER_GENERATOR="cinema-api"
export CLOUD_NAME="tu_cloud_name"
export CLOUD_KEY="tu_api_key"
export CLOUD_SECRET="tu_api_secret"
```

**Opción B — configurarlas en tu IDE** (IntelliJ: Run Configuration → Environment variables).

> ⚠️ Nunca subas estos valores reales a `application.properties` ni los commitees a git — es lo que este proyecto evita a propósito usando `${VARIABLE}`.

### 4. Cargar datos iniciales (seeder)

El proyecto incluye un seeder (`data.sql`) que crea roles, permisos, un usuario ADMIN y algunas películas de ejemplo. Para cargarlo la primera vez:

```properties
spring.sql.init.mode=always
```

Iniciá la aplicación una vez para que se ejecute el seeder, y luego vuelve a poner:

```properties
spring.sql.init.mode=never
```

para que no se vuelva a ejecutar en los próximos inicios (evita errores de datos duplicados).

Con el seeder cargado, podés loguearte directo con el usuario admin por defecto:
```
email: admin@cinema.com
password: admin123
```

### 5. Instalar Dependencias y Ejecutar
```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```
---

## 📖 Probar la API con Swagger

Con la aplicación corriendo, accedé a la documentación interactiva en:

```
http://localhost:8080/swagger-ui.html
```

### Autenticarte dentro de Swagger

La mayoría de los endpoints requieren un token JWT. Para probarlos:

1. Abrí el endpoint **`POST /api/auth/login`**, hacé clic en **"Try it out"** y enviá tus credenciales.
2. Copiá el valor de `jwt` de la respuesta (es el access token).
3. Hacé clic en el botón **"Authorize"** (ícono de candado, arriba a la derecha).
4. Pegá el token (sin la palabra `Bearer`, Swagger la agrega solo) y confirmá.

A partir de ahí, todos los endpoints que pruebes desde la UI van a incluir el token automáticamente en el header `Authorization`.

### Renovar el token (Refresh Token)

El login devuelve dos tokens:

- **`jwt`** (access token): dura **30 minutos**, es el que se usa en cada request.
- **`refreshToken`**: dura **7 días**, sirve únicamente para pedir un `jwt` nuevo sin tener que loguearte de nuevo.

Cuando el `jwt` expira (pasados los 30 min), llamá a **`POST /api/auth/refresh`** con el `refreshToken` guardado:

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

La respuesta trae un `jwt` nuevo (con otros 30 minutos de vida) y el mismo `refreshToken` de siempre. En Swagger, tenés que volver a hacer clic en **"Authorize"** y pegar el `jwt` nuevo manualmente — Swagger no refresca el token solo.

> El `refreshToken` no sirve para autenticar endpoints normales (`/api/movie`, `/api/user`, etc.) — si lo intentás usar ahí, la API lo rechaza. Solo es válido en `/api/auth/refresh`.

---

## 📋 Tareas Pendientes

### 1. Autenticación (Media Prioridad)

- [ ] Revocación de refresh tokens (logout real) - actualmente son stateless: si un `refreshToken` se filtra, sigue siendo válido hasta sus 7 días sin forma de invalidarlo antes
- [ ] Expiración por inactividad (idle timeout)

### 2. Testing (Media Prioridad)

- [x] Pruebas unitarias para servicios
- [ ] Pruebas de integración para endpoints
- [ ] Pruebas de seguridad (autenticación, autorización)
- [ ] Cobertura mínima: 80%

### 3. Docker Deployment (Media Prioridad)

- [ ] `Dockerfile` para la aplicación
- [ ] `docker-compose.yml` con servicio de MySQL
- [ ] Script de inicialización de BD
- [ ] Guía de deployment con Docker

---

## 📚 Referencias

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io)
- [OpenAPI/Swagger](https://swagger.io)
- [MySQL Documentation](https://dev.mysql.com/doc)

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.
