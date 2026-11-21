# Paolicias.Sweet · Backend

Microservicios REST para la gestión de catálogo, cotizaciones, reservas de pedidos y carga de comprobantes de pago de la aplicación móvil **Paolicias.Sweet**.

**DSY1105 Desarrollo de Aplicaciones Móviles · Duoc UC · Sección 012D · Equipo: González, Noriega, Obreque**

---

## Arquitectura

El backend está compuesto por **3 microservicios Spring Boot independientes** respaldados por **PostgreSQL** y orquestados mediante Docker Compose. Los servicios no se comunican entre sí; la aplicación Android (arquitectura MVVM + Retrofit) coordina las peticiones hacia cada servicio según el flujo del usuario.

| Microservicio | Carpeta | Puerto API | Responsabilidad |
| :--- | :--- | :---: | :--- |
| **Catálogo y Cotizador** | `catalogo-service/` | `8081` | Gestión de productos de pastelería, carga inicial (`DataLoader`) y cálculo de cotizaciones |
| **Reservas** | `reservas-service/` | `8082` | Creación de pedidos, consulta por usuario/administrador y gestión de estados |
| **Comprobantes** | `comprobantes-service/` | `8083` | Recepción y almacenamiento de comprobantes de transferencia (`Multipart`) |

Cada microservicio sigue la misma estructura en capas:

```text
config/      ->  carga de datos iniciales y configuración
controller/  ->  endpoints REST
dto/         ->  objetos de transferencia de datos (Request / Response)
model/       ->  entidades JPA / Hibernate
repository/  ->  acceso a datos (JpaRepository)
service/     ->  lógica de negocio
```

---

## Tecnologías

* **Java 21** y **Spring Boot** (Spring Web MVC, Spring Data JPA)
* **PostgreSQL**
* **Docker** y **Docker Compose**
* **JUnit 5**

---

## Endpoints

### 1. `catalogo-service` · `http://localhost:8081`

| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/productos` | Lista todos los productos disponibles en el catálogo |
| `GET` | `/api/productos/{id}` | Obtiene el detalle de un producto por su ID |
| `POST` | `/api/productos` | Crea un nuevo producto en el catálogo |
| `POST` | `/api/cotizar` | Calcula el valor total de una cotización según productos y cantidades |

---

### 2. `reservas-service` · `http://localhost:8082`

| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/reservas` | Lista todas las reservas (vista Administrador o filtradas por cliente) |
| `GET` | `/api/reservas/{id}` | Obtiene una reserva específica |
| `POST` | `/api/reservas` | Crea una nueva reserva en estado `PENDIENTE` |
| `PUT` / `PATCH` | `/api/reservas/{id}` | Actualiza el estado de la reserva (`APROBADA`, `RECHAZADA`, `ENTREGADA`) |

**Flujo de estados de una reserva:**

```text
PENDIENTE ──(aprobar)──> APROBADA ──(entregar)──> ENTREGADA
    │
    └──(rechazar)──> RECHAZADA
```

**Ejemplo de cuerpo para crear una reserva (`POST /api/reservas`):**

```json
{
  "clienteNombre": "Camila González",
  "clienteEmail": "camila@ejemplo.cl",
  "producto": "Torta Tres Leches 20 personas",
  "cantidad": 1,
  "fechaEntrega": "2026-11-28",
  "total": 28000
}
```

---

### 3. `comprobantes-service` · `http://localhost:8083`

| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/comprobantes` | Lista los comprobantes registrados |
| `POST` | `/api/comprobantes` | Sube una imagen o archivo de comprobante (`multipart/form-data`) asociado a una reserva |

---

## Cómo ejecutar en local

**Requisito:** Docker Desktop encendido.

1. Desde la raíz del repositorio (`paoliciassweet-backend/`), levanta los 3 microservicios y la base de datos PostgreSQL:
   ```bash
   docker compose up -d --build
   ```
2. Verifica que todos los contenedores estén activos:
   ```bash
   docker ps
   ```
3. Comprueba el funcionamiento abriendo `http://localhost:8081/api/productos` en el navegador.

Para detener los servicios:
```bash
docker compose down
```

---

## Pruebas unitarias (JUnit 5)

Cada microservicio incluye pruebas unitarias para validar la lógica de negocio y modelos de dominio:

| Microservicio | Clase de prueba | Qué valida |
| :--- | :--- | :--- |
| `catalogo-service` | `CotizadorServiceTest.java` | Cálculo de subtotales y totales en cotizaciones |
| `reservas-service` | `ReservaModelTest.java` | Creación, atributos y cambios de estado de reservas |
| `comprobantes-service` | `ComprobanteModelTest.java` | Registro y validación de datos de comprobantes |

Para ejecutar las pruebas dentro de la carpeta de cada microservicio (con PostgreSQL encendido):

```bash
./mvnw test
```
*(En Windows PowerShell: `.\mvnw.cmd test`)*

---

## Despliegue en la nube (Render + PostgreSQL)

* `catalogo-service`: *(pendiente URL HTTPS)*
* `reservas-service`: *(pendiente URL HTTPS)*
* `comprobantes-service`: *(pendiente URL HTTPS)*
