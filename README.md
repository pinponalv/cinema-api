# 🎬 Cinema API

API REST para la gestión de un cine, incluyendo películas, usuarios, autenticación y control de permisos basado en roles.

**Versión:** 0.0.1-SNAPSHOT | **Java:** 17+ | **Spring Boot:** 4.0.7

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Requisitos](#-requisitos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Endpoints API](#-endpoints-api)
- [Autenticación y Seguridad](#-autenticación-y-seguridad)
- [Base de Datos](#-base-de-datos)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Testing](#-testing)
- [Tareas Pendientes](#-tareas-pendientes)
- [Contribución](#-contribución)

---

## ✨ Características

- ✅ **Autenticación JWT** - Secure token-based authentication
- ✅ **Control de Acceso basado en Roles (RBAC)** - Usuarios, roles y permisos
- ✅ **Gestión de Películas** - CRUD completo
- ✅ **Spring Security** - Configuración de seguridad avanzada
- ✅ **OpenAPI/Swagger** - Documentación interactiva de API
- ✅ **MySQL Database** - Persistencia relacional
- ✅ **MapStruct** - Mapeo limpio entre DTOs y entidades
- ⏳ **Docker Support** - Containerización (en desarrollo)
- ⏳ **Unit Tests** - Cobertura de pruebas (en desarrollo)

---

## 📦 Requisitos

Antes de comenzar, asegúrate de tener instalado:

- **Java 17** o superior
- **Maven 3.6** o superior
- **MySQL 8.0** o superior
- **Git**

Verifica las versiones:
```bash
java -version
mvn -version
mysql --version
```

---

## 🚀 Instalación

### 1. Clonar el Repositorio
```bash
git clone <tu-repo-url>
cd cinema-api
```

### 2. Configurar la Base de Datos

Crea la base de datos en MySQL:
```sql
CREATE DATABASE cinema_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configurar Variables de Entorno

Edita `src/main/resources/application.yml` con tus credenciales:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cinema_db
    username: root
    password: tu_contraseña
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update  # change to 'validate' in production
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
  
  application:
    name: cinema-api

server:
  port: 8080

# JWT Configuration
app:
  jwt:
    secret: tu_jwt_secret_key_muy_segura_con_minimo_256_bits
    expiration: 86400000  # 24 horas en milisegundos
```

### 4. Instalar Dependencias y Ejecutar

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

La API estará disponible en: `http://localhost:8080`

---

## ⚙️ Configuración

### Propiedades de Aplicación

El archivo `application.yml` contiene la configuración principal:

| Propiedad | Descripción | Valor por Defecto |
|-----------|-------------|-------------------|
| `spring.datasource.url` | URL de conexión a BD | `jdbc:mysql://localhost:3306/cinema_db` |
| `spring.jpa.hibernate.ddl-auto` | Estrategia de DDL | `update` (desarrollo), `validate` (producción) |
| `server.port` | Puerto del servidor | `8080` |
| `app.jwt.secret` | Clave secreta de JWT | Requiere configuración |
| `app.jwt.expiration` | Expiración de token en ms | `86400000` (24h) |

### Spring Security

La aplicación utiliza **Spring Security** con **JWT** para autenticación stateless:

- Los tokens JWT se envían en el header `Authorization: Bearer <token>`
- Los tokens contienen información de usuario y roles
- La expiración es configurable (por defecto 24 horas)
- Las contraseñas se encriptan con **BCrypt**

### CORS (Cross-Origin Resource Sharing)

Para habilitar CORS, agregar a `SecurityConfiguration.java`:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://tudominio.com"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

---

## 📁 Estructura del Proyecto

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
│   │   ├── AuthService
│   │   ├── UserService
│   │   ├── MovieService
│   │   ├── RoleService
│   │   └── PermissionService
│   └── interfaces/          # Contratos de servicio
│       └── IMoviesService, etc.
│
├── repository/              # Acceso a datos (JPA)
│   ├── UserRepository
│   ├── MoviesRepository
│   ├── RolesRepository
│   └── PermissionRepository
│
├── entity/                  # Modelos de base de datos
│   ├── UserSec              # Entidad de usuario
│   ├── Movies               # Entidad de película
│   ├── Roles                # Entidad de rol
│   └── Permission           # Entidad de permiso
│
├── dto/                     # Transfer Objects (Request/Response)
│   ├── AuthLoginRequest     # Credenciales de login
│   ├── AuthResponse         # Respuesta de autenticación (con token)
│   ├── UserRequest          # Datos para crear/actualizar usuario
│   ├── UserResponse         # Respuesta de usuario
│   ├── MovieRequest         # Datos para crear/actualizar película
│   ├── MovieResponse        # Respuesta de película
│   ├── RoleRequest/Response # DTOs de rol
│   └── PermissionRequest/Response # DTOs de permisos
│
├── mapper/                  # Mapeo entre entidades y DTOs
│   └── UserMapper           # Convierte UserSec ↔ UserResponse
│
├── config/                  # Configuración
│   └── OpenApiConfig        # Swagger/OpenAPI
│
└── CinemaApiApplication.java # Clase principal
```

---

## 📡 Endpoints API

### Autenticación

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "usuario@ejemplo.com",
  "password": "miContraseña123"
}
```

**Respuesta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

#### Registrar Usuario
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "nuevoUsuario",
  "email": "usuario@ejemplo.com",
  "password": "ContraseñaSegura123!"
}
```

**Respuesta (201 Created):**
```json
{
  "id": 1,
  "email": "usuario@ejemplo.com",
  "username": "nuevoUsuario",
  "roles": [
    { "id": 2, "name": "USER" }
  ]
}
```

El usuario se crea siempre con el rol por defecto (USER); el campo `roles` del request se ignora en este endpoint.

### Películas

#### Listar todas las películas
```http
GET /api/movies
Authorization: Bearer <tu_token>
```

**Respuesta (200 OK):**
```json
[
  {
    "id": 1,
    "title": "La Matriz",
    "director": "Wachowski",
    "year": 1999,
    "duration": 136
  }
]
```

#### Obtener película por ID
```http
GET /api/movies/{id}
Authorization: Bearer <tu_token>
```

#### Crear película (requiere rol ADMIN)
```http
POST /api/movies
Authorization: Bearer <tu_token>
Content-Type: application/json

{
  "title": "Inception",
  "director": "Christopher Nolan",
  "year": 2010,
  "duration": 148
}
```

#### Actualizar película
```http
PUT /api/movies/{id}
Authorization: Bearer <tu_token>
Content-Type: application/json

{
  "title": "Inception Updated",
  "director": "Christopher Nolan",
  "year": 2010,
  "duration": 150
}
```

#### Eliminar película
```http
DELETE /api/movies/{id}
Authorization: Bearer <tu_token>
```

### Usuarios

#### Listar usuarios (ADMIN, MOD)
```http
GET /api/users
Authorization: Bearer <tu_token>
```

#### Obtener usuario por id (ADMIN, MOD)
```http
GET /api/users/{id}
Authorization: Bearer <tu_token>
```

#### Crear usuario (ADMIN)
```http
POST /api/users
Authorization: Bearer <tu_token>
Content-Type: application/json

{
  "username": "nuevoUsuario",
  "email": "usuario@ejemplo.com",
  "password": "ContraseñaSegura123!",
  "roles": [
    { "id": 2 }
  ]
}
```

A diferencia de `/api/auth/register`, acá el campo `roles` es obligatorio (al menos un id) y sí se respeta.

#### Actualizar usuario (ADMIN)
```http
PATCH /api/users/user/{id}
Authorization: Bearer <tu_token>
Content-Type: application/json
```

Todos los campos son opcionales; solo se actualizan los que vengan en el body.

#### Eliminar usuario (ADMIN, MOD)
```http
DELETE /api/users/{id}
Authorization: Bearer <tu_token>
```

> No existe un endpoint `GET /api/users/me` para obtener el usuario autenticado actual.

### Roles y Permisos

Los permisos son independientes (`CREATE_MOVIE`, `READ`, etc.) y cada rol se define con el nombre del rol y el **conjunto de IDs de permisos** que lo componen. Por eso primero hay que crear los permisos y recién después el rol que los agrupa.

#### Crear permiso
```http
POST /api/permissions
Authorization: Bearer <tu_token>
Content-Type: application/json

{
  "permission": "CREATE_MOVIE"
}
```

**Respuesta (201 Created):**
```json
{
  "id": 3,
  "permission": "CREATE_MOVIE"
}
```

#### Listar permisos
```http
GET /api/permissions
Authorization: Bearer <tu_token>
```

#### Crear rol (asociando permisos existentes por id)
```http
POST /api/roles
Authorization: Bearer <tu_token>
Content-Type: application/json

{
  "role": "MODERATOR",
  "permissions": [
    { "id": 1 },
    { "id": 3 }
  ]
}
```

**Respuesta (201 Created):**
```json
{
  "id": 2,
  "role": "MODERATOR",
  "permissions": [
    { "id": 1, "permission": "READ" },
    { "id": 3, "permission": "CREATE_MOVIE" }
  ]
}
```

#### Listar roles
```http
GET /api/roles
Authorization: Bearer <tu_token>
```

#### Buscar rol por id
```http
GET /api/roles/role/{id}
Authorization: Bearer <tu_token>
```

Un usuario obtiene sus permisos de forma indirecta: `UserSec` tiene un conjunto de `Roles`, y cada `Roles` tiene su propio conjunto de `Permission`. No existe un endpoint para asignar un permiso suelto a un usuario; siempre se hace a través del rol.

---

## 🔐 Autenticación y Seguridad

### Flujo de Autenticación

```
1. Usuario → POST /api/auth/login con credenciales
2. Server → Valida credenciales en BD
3. Server → Genera JWT token (válido 24h)
4. Server → Retorna token al cliente
5. Cliente → Envía token en header Authorization: Bearer <token>
6. Server → Valida token en cada request
7. Server → Si token válido, procesa la solicitud
8. Server → Si token expirado/inválido, retorna 401 Unauthorized
```

### Estructura del JWT Token

El token contiene:
- `sub` - Subject (identificador de usuario)
- `iat` - Issued At (fecha de emisión)
- `exp` - Expiration (fecha de expiración)
- `roles` - Lista de roles del usuario

### Ejemplo con cURL

```bash
# 1. Login y obtener token
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  | jq -r '.token')

# 2. Usar el token en siguientes requests
curl -X GET http://localhost:8080/api/movies \
  -H "Authorization: Bearer $TOKEN"
```

### Roles y Permisos

| Rol | Descripción | Permisos Típicos |
|-----|-------------|------------------|
| `ADMIN` | Administrador | Crear/editar/eliminar películas, usuarios, roles |
| `MODERATOR` | Moderador | Editar películas, ver usuarios |
| `USER` | Usuario estándar | Ver películas, ver perfil |
| `CLIENT` | Cliente/Visitante | Ver películas (lectura) |

---

## 🗄️ Base de Datos

### Diagrama de Entidades (ER)

```
┌─────────────┐
│   UserSec   │
├─────────────┤
│ id (PK)     │
│ username    │
│ email       │
│ password    │
│ createdAt   │
└──────┬──────┘
       │ M:N
       ├──────────┐
       │          │
       v          v
   ┌────────┐  ┌─────────┐
   │ Roles  │  │ Movies  │
   ├────────┤  ├─────────┤
   │ id(PK) │  │ id(PK)  │
   │ name   │  │ title   │
   └────┬───┘  │ director│
        │      │ year    │
        │ M:N  │ duration│
        │      └─────────┘
        │
   ┌────v─────┐
   │Permission│
   ├──────────┤
   │ id (PK)  │
   │ name     │
   └──────────┘
```

### Scripts SQL Útiles

#### Crear usuario admin de prueba
```sql
-- La contraseña debe ir encriptada con BCrypt (no se puede insertar en texto plano)
INSERT INTO users (username, email, password)
VALUES ('admin', 'admin@ejemplo.com', '$2a$10$...');
```

#### Ver estructura de tablas
```sql
DESC users;
DESC movies;
DESC roles;
DESC permissions;
```

---

## 💡 Ejemplos de Uso

### Con curl

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# Listar películas
curl -X GET http://localhost:8080/api/movies \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."

# Crear película
curl -X POST http://localhost:8080/api/movies \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Interestelar",
    "director": "Christopher Nolan",
    "year": 2014,
    "duration": 169
  }'
```

### Swagger/OpenAPI UI

Accede a la documentación interactiva:
```
http://localhost:8080/swagger-ui.html
```

Aquí puedes:
- Ver todos los endpoints
- Probar cada endpoint directamente
- Ver esquemas de request/response
- Configurar el token Bearer

---

## 🧪 Testing

### Ejecutar pruebas unitarias
```bash
mvn test
```

### Cobertura de pruebas
```bash
mvn test jacoco:report
# Ver reporte en: target/site/jacoco/index.html
```

### Tests en desarrollo
- [ ] Tests de autenticación
- [ ] Tests de endpoints de película
- [ ] Tests de validaciones
- [ ] Tests de seguridad

---

## 📋 Tareas Pendientes

### 1. Testing (Media Prioridad)

- [ ] Pruebas unitarias para servicios
- [ ] Pruebas de integración para endpoints
- [ ] Pruebas de seguridad (autenticación, autorización)
- [ ] Cobertura mínima: 80%

### 2. Docker Deployment (Media Prioridad)

- [ ] `Dockerfile` para la aplicación
- [ ] `docker-compose.yml` con servicio de MySQL
- [ ] Script de inicialización de BD
- [ ] Guía de deployment con Docker

### 3. Documentación (Baja Prioridad - En Progreso)

- [x] Mejorar README.md
- [ ] Troubleshooting y FAQ

---

## 🤝 Contribución

### Antes de hacer un PR

1. **Crea una rama** desde `main`:
   ```bash
   git checkout -b feature/tu-feature-name
   ```

2. **Realiza los cambios** y comitea:
   ```bash
   git add .
   git commit -m "feat: agregar nueva funcionalidad"
   ```

3. **Haz push**:
   ```bash
   git push origin feature/tu-feature-name
   ```

4. **Abre un Pull Request** con:
   - Descripción clara de cambios
   - Referencia a issues relacionados
   - Tests incluidos

### Estándares de Código

- **Naming**: camelCase para variables/métodos, PascalCase para clases
- **Format**: Usar `mvn spotless:apply` (si configurado)
- **Documentación**: Javadoc para métodos públicos
- **Tests**: Mínimo 80% de cobertura

### Checklist antes de mergear

- [ ] Tests pasan (`mvn test`)
- [ ] Código compila sin errores/warnings
- [ ] README actualizado si aplica
- [ ] Commits con mensajes claros
- [ ] Sin cambios sin documentar

---

## 🐛 Troubleshooting

### Problema: "Connection refused" en BD

**Solución:**
```bash
# Verifica que MySQL esté corriendo
mysql -u root -p -e "SELECT 1;"

# O inicia MySQL:
# En macOS: brew services start mysql-server
# En Linux: sudo systemctl start mysql
```

### Problema: Token JWT expirado

**Solución:**
- Obtener nuevo token haciendo login nuevamente
- Aumentar expiración en `application.yml`: `app.jwt.expiration: 259200000` (72h)

### Problema: Acceso denegado (403 Forbidden)

**Solución:**
- Verifica que tienes el token válido en header
- Confirma que tu usuario tiene el rol requerido
- Revisa los permisos asignados en BD

### Problema: Swagger no carga

**Solución:**
```bash
# Limpia caché de Maven y reconstruye
mvn clean package
mvn spring-boot:run
```

Accede a: `http://localhost:8080/swagger-ui.html`

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

---

**Última actualización:** 2026-07-27 | **Versión:** 0.0.1-SNAPSHOT
