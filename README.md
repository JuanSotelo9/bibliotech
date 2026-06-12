# Proyecto-Modelos-1
Proyecto desarrollado para la asignatura Modelos de Programación 1. Contiene un backend Java (servlets) que gestiona usuarios, documentos y reservas, y un frontend estático con las vistas del cliente.

**Requisitos**
- **Java:** JDK 21+
- **Maven:** 3.x
- **Base de datos:** PostgreSQL (por defecto el proyecto usa `LibreriaDB` en `localhost:5432`)
- **Herramienta para servir archivos estáticos:** `python3` (opcional) o extensión Live Server en VS Code

**Instalación y configuración**
- Clona el repositorio.
- Crea la base de datos y tablas ejecutando el script SQL: [libreriaDB.sql](libreriaDB.sql)
	- Ejemplo con `psql`:

```bash
# desde la carpeta del proyecto
psql -U postgres -c "CREATE DATABASE \"LibreriaDB\";"
psql -U postgres -d LibreriaDB -f libreriaDB.sql
```

- Configurar credenciales de conexión a la base de datos:
	- Por defecto las credenciales están en `ConexionDB.java`. Archivo: [backend/Libreria/src/main/java/modelo/persistencia/ConexionDB.java](backend/Libreria/src/main/java/modelo/persistencia/ConexionDB.java#L1-L120)
	- Recomendación: cambiar la contraseña y/o mover la configuración a variables de entorno o un fichero de propiedades antes de desplegar.

**Construir y ejecutar el backend**
- Compilar con Maven:

```bash
mvn -f backend/Libreria/pom.xml clean package
```

- El empaquetado genera artefactos en `backend/Libreria/target/`. Si el proyecto está configurado como `.war`, desplegar en Tomcat/servlet container. Alternativamente ejecutar desde un IDE que soporte servlets.

**Ejecutar el frontend**
- Abrir `frontend/index.html` en el navegador o servir la carpeta `frontend` con un servidor estático:

```bash
cd frontend
python -m http.server 8000
# luego abrir http://localhost:8000
```

**Endpoints principales (REST-like servlets)**
- `POST /usuario/registrar` : registra un nuevo usuario.
- `POST /usuario/login` : login, devuelve token JWT.
- `GET /usuario/datos` : obtiene datos del usuario (requiere autenticación).
- `GET /usuario/documentos` : lista documentos del usuario.
- `GET /usuario/reservas` : lista reservas del usuario.
- `POST /usuario/consultar` : consulta de usuario por criterios.

- `POST /documento/crear` : crear documento (requiere autorización).
- `POST /documento/modificar` : modificar documento.
- `POST /documento/reservar` : reservar documento.
- `POST /documento/entregar` : registrar devolución/entrega.
- `POST /documento/eliminar` : eliminar documento.
- `POST /documento/habilitar` : habilitar documento.
- `POST /documento` : obtener documento por id/payload.
- `POST /documento/eventos` : buscar eventos relacionados.
- `POST /documento/titulo` : buscar por título.

Los servlets principales están en `backend/Libreria/src/main/java/Servlets/` (`ServletUsuario.java`, `ServletDocumentos.java`).

**Ejemplo rápido (curl)**
- Login:

```bash
curl -X POST http://localhost:8080/usuario/login -d '{"email":"ej@ej.com","password":"pass"}' -H "Content-Type: application/json"
```

- Obtener documentos (GET):

```bash
curl http://localhost:8080/usuario/documentos
```

**Estructura del repositorio**
- **backend/Libreria/**: código Java, `pom.xml` (compilado con `release` 21). Ver [backend/Libreria/pom.xml](backend/Libreria/pom.xml#L1-L50).
- **frontend/**: vistas y assets (HTML, JS, CSS).
- **libreriaDB.sql**: script de creación de la base de datos.

**Buenas prácticas / Notas importantes**
- No deje credenciales (usuario/contraseña) en el control de versiones; el proyecto actualmente incluye credenciales por defecto en `ConexionDB.java`. Cámbielas antes de compartir el repositorio.
- Añada un mecanismo de configuración por entorno (variables de entorno o fichero `application.properties`).

**Contribuir**
- Hacer fork/branch, abrir pull request con descripción clara de cambios y pasos para reproducir.

**Contacto**
- Autor: repositorio de la asignatura. Para dudas abrir issue en el repositorio.

---
Para más detalles ver los ficheros de implementación y los servlets en `backend/Libreria/src/main/java/`.
