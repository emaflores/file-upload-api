# File Upload API

Este es un proyecto de una API que permite subir, descargar, actualizar y borrar archivos a través de endpoints HTTP. La API utiliza dos algoritmos de hash (SHA-256 y SHA-512) para calcular el hash de los archivos subidos y guardarlos en una base de datos embebida.

## Tecnologías utilizadas

- Java 11
- Spring Boot 2.5.0
- PostgreSQL 
- JUnit 4
- Swagger

## Funcionalidades

La API proporciona los siguientes endpoints para manejar archivos:

- `POST /api/documents`: Endpoint para subir uno o varios archivos con el tipo de hash especificado. Este endpoint acepta archivos de cualquier tipo.
- `GET /api/documents`: Endpoint para obtener una lista de todos los archivos subidos.
- `GET /api/document`: Endpoint para buscar un archivo por su hash.
- `PUT /api/document`: Endpoint para actualizar un archivo por su hash.
- `DELETE /api/document`: Endpoint para borrar un archivo por su hash.

Para más información sobre el formato de los datos, se puede consultar la documentación de Swagger.

## Documentación

La API utiliza Swagger para generar una documentación interactiva. Para acceder a la documentación, simplemente abra un navegador web y vaya a `http://localhost:8080/swagger-ui.html`.

La documentación proporciona una descripción detallada de cada endpoint, incluyendo los parámetros de entrada, los códigos de respuesta y un ejemplo de respuesta en formato JSON.

## Ejecución

Para ejecutar la API, se debe seguir los siguientes pasos:

1. Descargar el proyecto y abrirlo en una IDE que tenga soporte para Java 11.
2. En la consola, ir a la carpeta raíz del proyecto y ejecutar el comando `mvn spring-boot:run`.
3. La API estará disponible en `http://localhost:8080`.

## Pruebas unitarias

El proyecto incluye pruebas unitarias escritas con JUnit 4. Para ejecutar las pruebas, se debe seguir los siguientes pasos:

1. En la consola, ir a la carpeta raíz del proyecto y ejecutar el comando `mvn test`.
