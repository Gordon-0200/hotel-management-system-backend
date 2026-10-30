# Hotel Management System Backend

- **Names:** Muhirwa Gordon
- **Student ID:** 27648
- **Project name:** Hotel Management System
- **Course:** INSY 8322 Web Technology and Internet (Mid-Term Project)

A Spring Boot REST API that manages guests, receptionists, skills, front desks,
reservations and room orders of a hotel.

## Technologies
- Java 21
- Spring Boot (Spring Web, Spring Data JPA)
- PostgreSQL
- Maven

## Steps to run the API

1. Install Java 21, Maven (or use the included `mvnw`) and PostgreSQL.
2. Clone the repository:
   ```bash
   git clone https://github.com/Gordon-0200/hotel-management-system-backend.git
   cd hotel-management-system-backend
   ```
3. Create the database in PostgreSQL:
   ```sql
   CREATE DATABASE hotel__db;
   ```
4. Open `src/main/resources/application.properties` and set your database name,
   username and password:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/hotel__db
   spring.datasource.username=gordon
   spring.datasource.password=3000
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```
5. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   On Windows use `mvnw.cmd spring-boot:run`.
6. The API starts on `http://localhost:5000` and the tables are created automatically.
7. Import `hotel-management-system-midterm.postman_collection.json` into Postman.
   It has one request per requirement (A2, B1, C4, D2, ...).

## Main endpoints
- `/api/skills`, `/api/front-desks`, `/api/receptionists`, `/api/guests`, `/api/reservations`
- `/api/room-orders`
- `/api/dashboard/summary`
