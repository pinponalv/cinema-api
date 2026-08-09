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

- ✅ **Autenticación JWT** - Secure token-based authentication
- ✅ **Control de Acceso basado en Roles (RBAC)** - Usuarios, roles y permisos
- ✅ **Gestión de Películas** - CRUD completo, con búsqueda por título y por género
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
CREATE DATABASE `cinema-api` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configurar credenciales

Editá `src/main/resources/application.properties` con tus credenciales de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cinema-api
spring.datasource.username=root
spring.datasource.password=tu_contraseña
```

### 4. Instalar Dependencias y Ejecutar
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
2. Copiá el valor de `token` de la respuesta.
3. Hacé clic en el botón **"Authorize"** (ícono de candado, arriba a la derecha).
4. Pegá el token (sin la palabra `Bearer`, Swagger la agrega solo) y confirmá.

A partir de ahí, todos los endpoints que pruebes desde la UI van a incluir el token automáticamente en el header `Authorization`.

> Si no tenés ningún usuario todavía, primero registrá uno con **`POST /api/auth/register`** y después hacé login con esas credenciales.

---

## 📋 Tareas Pendientes

### 1. Testing (Media Prioridad)

- [x] Pruebas unitarias para servicios
- [ ] Pruebas de integración para endpoints
- [ ] Pruebas de seguridad (autenticación, autorización)
- [ ] Cobertura mínima: 80%

### 2. Docker Deployment (Media Prioridad)

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
