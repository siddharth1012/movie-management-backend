Pull the Postgres Image from Docker Desktop. 
Run the following command in the source folder: 
docker run -itd -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=123 -p 5432:5432 --name mypostgres postgres

For accessing Swagger UI: 
go to the following link: http://localhost:8080/swagger-ui/index.html


Features: 

Authentication and Authorization
Swagger UI for API Documentation
JWT Token Generation using CustomSecurity

