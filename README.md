# Task Management API Suite

This is a maven project which uses an embedded derby database to store tasks in a table.

The table structure is provided below:

**Table name** - *tasks*

**Table columns:**
- *id* int not null generated always as identity,
- *title* varchar(256) not null,
- *description* varchar(1024),
- *due_date* date,
- *status* varchar(10),
- *creation_date* date not null,
- *primary key (id)*

## API endpoints

You will need to provide APIs for the following actions:

1. Fetch all tasks.
1. Fetch all overdue tasks.
1. Fetch data for a single task.
1. Add a new task.
1. Modify a task.
1. Delete a task.

## Pre-requisites
1. Java needs to be installed on the system. This project was developed against Java 19.0.1, however the minimum version of Java that can be used is Java 17, as that is a requirement for Spring Boot 3.
   Check by running below command in command prompt  
   `java -version`
2. Maven needs to be installed on the system.  This project has been developed using Maven 3.8.6
   Check by running below command in command prompt  
   `mvn -v`

## Technology choice
- This is a modern Spring Boot application, version 3.0. 

## Run the application
- The application can be compiled and run from the command line using the Maven spring boot plugin:
  `mvn spring-boot:run`
- However, to create an artefact (including running the tests) that can be deployed into production:
  `mvn clean package`
  `java -jar target/springboot-reference-1.0.0.jar`
- The application API will be available at [http://localhost:8080/api/tasks](http://localhost:8080/api/tasks)

## Assumptions
- A frontend is not required, although this can be done as per https://www.baeldung.com/spring-mvc-static-resources
- Complete test coverage is not possible in the time taken to build this app. We can consider the following test pyramid:
    - unit tests for most classes, including mockMvc to test the controller and DTO logic in detail. 
    - integration tests for data access using an embedded or in memory db, or a separate containerized db.
    - limited end-to-end tests to show the behaviour of the API.

## Is this a production grade API?
- I would not expect to see `spring.jpa.hibernate.ddl-auto=update` enabled in a production application, nor `spring.jpa.show-sql=true`. I would recommend to manage updating database schema outside the application using Liquibase or Flyway. We might use `spring.jpa.hibernate.ddl-auto=validate` to cause the app to fail to startup should the database schema not match the expectation of the ORM.
- The database should not be embedded into the application, but externalised and made production grade.
- The build and deployment of the application can be containerized to make explicit the broader dependencies of the application.
- Fetch all API endpoints should be limited or paged to prevent excessive resource use.
- It is often considered good practice not to expose the database PK value to a public API, so DB engines can be changed etc.

# Things to do:

- Makefile

