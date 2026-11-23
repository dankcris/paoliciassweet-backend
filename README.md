# Paolicias.Sweet · Backend

Microservicios REST (Spring Boot) para catálogo y cotización, reservas y comprobantes de pago de la app móvil **Paolicias.Sweet**.

**DSY1105 Desarrollo de Aplicaciones Móviles · Duoc UC · Sección 012D · Equipo: González, Noriega, Obreque**

## Arquitectura

3 microservicios independientes con PostgreSQL (Supabase en la nube / contenedor local en desarrollo). La app Android (MVVM + Retrofit) coordina las llamadas.

| Microservicio | Carpeta | Puerto local | Responsabilidad |
| :--- | :--- | :---: | :--- |
| Catálogo y Cotizador | `catalogo-service/` | `8081` | Productos y cálculo de cotizaciones |
| Reservas | `reservas-service/` | `8082` | CRUD de reservas y cambio de estado |
| Comprobantes | `comprobantes-service/` | `8083` | Recepción de comprobantes (`multipart`), guardados en PostgreSQL (`bytea`) |

Capas por servicio: `controller` → `service` → `repository` → `model` (+ `dto`, `config`).

**Tecnologías:** Java 17, Spring Boot, Spring Data JPA, PostgreSQL, Docker, JUnit 5, Mockito.

## Endpoints

### catalogo-service
| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/productos` | Lista los productos |
| `GET` | `/api/productos/{id}` | Detalle de un producto |
| `POST` | `/api/cotizaciones` | Calcula la cotización `{productoId, asistentes}` |

### reservas-service
| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/reservas?email=` | Lista reservas (todas, o por correo del cliente) |
| `GET` | `/api/reservas/{id}` | Obtiene una reserva |
| `POST` | `/api/reservas` | Crea una reserva (estado `PENDIENTE`) |
| `PUT` | `/api/reservas/{id}` | Actualiza los datos de la reserva |
| `PUT` | `/api/reservas/{id}/estado` | Cambia el estado `{"estado": "..."}` |
| `DELETE` | `/api/reservas/{id}` | Elimina la reserva (`204`) |

Estados válidos: `PENDIENTE`, `COMPROBANTE_ENVIADO`, `EN_REVISION`, `ABONO_50`, `COMPLETADO`, `CANCELADA`.

### comprobantes-service
| Método | Ruta | Descripción |
| :--- | :--- | :--- |
| `POST` | `/api/comprobantes` | Sube el comprobante (`reservaId`, `montoAbonado`, `archivo`) |
| `GET` | `/api/comprobantes` | Lista los comprobantes |
| `GET` | `/api/comprobantes/reserva/{reservaId}` | Comprobantes de una reserva |
| `GET` | `/api/comprobantes/{id}/archivo` | Descarga la imagen |

## Ejecutar en local (Docker)

```bash
docker compose up -d --build
```
Verifica en `http://localhost:8081/api/productos`. Sin archivo `.env`, usa el PostgreSQL local del compose.

## Base de datos en Supabase

1. Crear un proyecto en [supabase.com](https://supabase.com) y guardar la contraseña de la base de datos.
2. En **Connect** copiar los datos del **Session pooler** (host `aws-0-<region>.pooler.supabase.com`, puerto `5432`).
3. Variables de entorno de cada servicio:
   ```
   SPRING_DATASOURCE_URL=jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres?sslmode=require
   SPRING_DATASOURCE_USERNAME=postgres.<project-ref>
   SPRING_DATASOURCE_PASSWORD=<contraseña>
   ```
   Para Docker local: copiar `.env.example` a `.env` y completar.
4. Al arrancar, Hibernate crea las tablas (`ddl-auto=update`) y el catálogo se carga con `DataLoader`.
5. Ejecutar `supabase/enable_rls.sql` en el SQL Editor de Supabase.

## Despliegue en Render

1. Render → **New → Blueprint** → seleccionar este repositorio (usa `render.yaml`).
2. Completar las 3 variables de Supabase en cada servicio.
3. URLs públicas (HTTPS):
   * `catalogo-service`: https://paoliciassweet-catalogo.onrender.com
   * `reservas-service`: https://paoliciassweet-reservas.onrender.com
   * `comprobantes-service`: https://paoliciassweet-comprobantes.onrender.com

   Si Render asigna otro nombre, actualizar `catalogoUrlCloud`, `reservasUrlCloud` y `comprobantesUrlCloud` en `gradle.properties` de la app. El plan gratuito se "duerme": la primera petición puede tardar ~50 s.

## Pruebas unitarias

```bash
cd catalogo-service && ./mvnw test
cd reservas-service && ./mvnw test
cd comprobantes-service && ./mvnw test
```
Las pruebas usan H2 en memoria y Mockito; no requieren PostgreSQL.

| Servicio | Clases de prueba |
| :--- | :--- |
| catalogo-service | `CotizadorServiceTest` |
| reservas-service | `ReservaServiceTest`, `ReservaModelTest` |
| comprobantes-service | `ComprobanteServiceTest`, `ComprobanteModelTest` |
