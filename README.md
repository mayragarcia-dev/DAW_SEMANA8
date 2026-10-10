# Laboratorio: Implementación de Tests con Spring Boot

## Descripción
Proyecto Spring Boot 3.5.6 que implementa un CRUD completo para **Productos** y **Categorías** con pruebas unitarias (Mockito) y de integración (MockMvc + @SpringBootTest).

---

## Tecnologías
- Java 17
- Spring Boot 3.5.6
- Spring Data JPA
- MySQL / H2 (tests)
- Maven
- JUnit 5 + Mockito
- Jakarta Validation

---

## Estructura del Proyecto

```
src/
├── main/java/com/tecsup/demo02/
│   ├── controller/
│   │   ├── ProductoController.java
│   │   └── CategoriaController.java
│   ├── dto/
│   │   ├── ProductoDTO.java
│   │   └── CategoriaDTO.java
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   ├── model/
│   │   ├── Producto.java
│   │   └── Categoria.java
│   ├── repository/
│   │   ├── ProductoRepository.java
│   │   └── CategoriaRepository.java
│   ├── service/
│   │   ├── ProductoService.java
│   │   └── CategoriaService.java
│   └── Demo02Application.java
└── test/java/com/tecsup/demo02/
    ├── controller/
    │   ├── ProductoControllerTest.java
    │   └── CategoriaControllerTest.java
    ├── service/
    │   ├── ProductoServiceTest.java
    │   └── CategoriaServiceTest.java
    └── Demo02ApplicationTests.java
```

---

## Endpoints

### Productos (`/api/productos`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/productos` | Crear producto |
| GET | `/api/productos` | Listar todos |
| GET | `/api/productos/{id}` | Obtener por ID |
| GET | `/api/productos/nombre/{nombre}` | Buscar por nombre |
| PUT | `/api/productos/{id}` | Actualizar |
| DELETE | `/api/productos/{id}` | Eliminar |

### Categorías (`/api/categorias`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/categorias` | Crear categoría |
| GET | `/api/categorias` | Listar todas |
| GET | `/api/categorias/{id}` | Obtener por ID |
| GET | `/api/categorias/nombre/{nombre}` | Buscar por nombre |
| PUT | `/api/categorias/{id}` | Actualizar |
| DELETE | `/api/categorias/{id}` | Eliminar |

---

## Validaciones

### ProductoDTO
- `nombre`: @NotBlank
- `precio`: @Positive
- `stock`: @Min(0)
- `categoria`: @NotBlank

### CategoriaDTO
- `nombre`: @NotBlank, @Size(max=100)
- `descripcion`: @Size(max=500)

---

## Manejo de Excepciones
- **404 Not Found**: Recurso no encontrado
- **400 Bad Request**: Validación fallida
- **409 Conflict**: Nombre duplicado (Categoría)

---

## Ejecución

### Requisitos
- JDK 17+
- Maven 3.8+

### Compilar y testear
```bash
./mvnw clean test
```

### Ejecutar aplicación
```bash
./mvnw spring-boot:run
```
La app inicia en `http://localhost:8080` (usa H2 en memoria para tests, MySQL en producción).

---

## Tests Implementados (37 total)

### ProductoServiceTest (7 unitarios)
- `testListarProductos`
- `testGuardarProducto`
- `testObtenerProducto`
- `testEliminarProducto`
- `testBuscarPorNombre`
- `testObtenerProductoNoExistente`
- `testActualizarProducto`

### CategoriaServiceTest (7 unitarios)
- `testListarCategorias`
- `testGuardarCategoria`
- `testObtenerCategoria`
- `testObtenerCategoriaNoExistente`
- `testEliminarCategoria`
- `testBuscarPorNombre`
- `testExistePorNombre`

### ProductoControllerTest (10 integración)
- `testGuardarProducto`
- `testListarProductos`
- `testValidacionNombreVacio`
- `testValidacionPrecioNegativo`
- `testValidacionCategoriaVacia`
- `testActualizarProducto`
- `testActualizarProductoNoExistente`
- `testValidacionActualizacion`
- `testEliminarProducto`
- `testEliminarProductoNoExistente`

### CategoriaControllerTest (12 integración)
- `testGuardarCategoria`
- `testGuardarCategoriaDuplicada`
- `testListarCategorias`
- `testObtenerCategoriaPorId`
- `testObtenerCategoriaNoExistente`
- `testBuscarPorNombre`
- `testActualizarCategoria`
- `testActualizarCategoriaNoExistente`
- `testEliminarCategoria`
- `testValidacionNombreVacio`
- `testValidacionNombreMuyLargo`
- `testValidacionDescripcionMuyLarga`

### Demo02ApplicationTests (1 contexto)
- `contextLoads`

---

## Base de Datos

### Producción (MySQL)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/demo02
spring.datasource.username=root
spring.datasource.password=secret
```

### Tests (H2 en memoria)
Configurado automáticamente en `application.properties` de test.

---

## Autor
**Mayra Garcia** - CICLO 4 - SECCIÓN B - Lab05