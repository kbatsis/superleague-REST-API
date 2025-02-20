# superleague-REST-API

A Service Oriented Architecture REST API for managing greek soccer championship data. Written in Java 17 and Spring Boot 3.3.4. The application can be deployed via Docker compose and endpoint documentation is provided with Swagger-ui.

Create, update and delete operations are secured by a JSON Web Token mechanism. To get a JWT a POST request can be made at "/api/login" with the following body:

{
    "username": "admin",
    "password": "demopass"
}
