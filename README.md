# Tool Rental Application
## Overview

The Tool Rental Application is a Spring Boot-based application designed to manage the rental of tools. It supports operations such as storing tool information, calculating rental charges based on specific rules, and generating rental agreements.

## Features

- **Tool Management**: Store and manage tools including their type, brand, and rental charges.
- **Rental Agreement Generation**: Generate rental agreements that include details like rental days, discounts, and final charges.
- **Holiday Considerations**: Account for holidays and weekends in rental charge calculations.

## Project Structure


Here is a sample README.md file for the Tool Rental project. This file provides an overview of the project, instructions on how to set it up, run it, and includes details on how the code is organized.

markdown
Copy code
## Project Structure

src/
├── main/
│ ├── java/
│ │ └── com/
│ │ └── example/
│ │ └── toolrental/
│ │ ├── ToolRentalApplication.java # Main Spring Boot application class
│ │ ├── config/
│ │ │ └── DatabaseInitializer.java # Initializes the database with sample data
│ │ ├── model/
│ │ │ └── Tool.java # JPA entity for Tool
│ │ ├── repository/
│ │ │ └── ToolRepository.java # JPA repository for Tool entity
│ │ ├── service/
│ │ │ └── ToolService.java # Business logic for tool management
│ │ └── controller/
│ │ └── ToolController.java # REST controller for handling tool-related API requests
│ └── resources/
│ ├── application.properties # Spring Boot application configuration
│ ├── schema.sql # Database schema definition
│ └── data.sql # Initial data for the database
└── test/
└── java/
└── com/
└── example/
└── toolrental/
├── ToolRepositoryIntegrationTest.java # Integration tests for ToolRepository
├── DatabaseDumpTest.java # Test for dumping database contents
└── ToolRepositoryDataLoadTest.java # Test for verifying data load on startup

 Generate OpenAPI Documentation (Optional)
You can also generate the OpenAPI documentation in JSON or YAML format by accessing:

Swagger: 
http://localhost:8080/swagger-ui/index.html#/checkout-controller

JSON: http://localhost:8080/v3/api-docs
YAML: http://localhost:8080/v3/api-docs.yaml

## Prerequisites

- Java 18 or later
- Maven 3.6 or later
- Docker (optional, for containerized deployment)

## Setup

### Clone the Repository

[//]: # (```bash)
# Tool Rental Swagger

git clone https://github.com/gsoto82/gs0816.git
cd gs0816

mvn clean install
mvn spring-boot:run
mvn clean package
java -jar target/gs0816-0.0.1-SNAPSHOT.jar

Accessing the H2 Database Console
If enabled, you can access the H2 database console at 
http://localhost:8080/h2-console using the following credentials:

JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: 
Database Initialization
On startup, the application automatically initializes the database schema using schema.sql and populates it with sample data from data.sql.

Testing
The project includes unit and integration tests to verify the correctness of the tool rental functionality.

Run Tests
You can run all tests using Maven:

mvn test

Sample Data
The following sample tools are pre-loaded into the database:

CHNS: Chainsaw (Stihl) - $1.49/day
LADW: Ladder (Werner) - $1.99/day
JAKD: Jackhammer (DeWalt) - $2.99/day
JAKR: Jackhammer (Ridgid) - $2.99/day

API Endpoints

Get Tool by Code
URL: api/rental/checkout/
Query Parameters:
    toolCode (e.g., "CHNS")
    rentalDays (e.g., 5)
    discountPercent (e.g., 10)
    checkoutDate (e.g., "07/02/2020")

Method: POST
Description: Returns details of a specific tool based on the tool code.

Example:
http://localhost:8080/api/v1/rental/checkout?toolCode=JAKD&rentalDays=6&discountPercent=0&checkoutDate=07/02/2020


### Customization and Usage

- **GitHub URL**: Replace `https://github.com/gsoto82/gs0816.git` with the actual URL of your repository.
- **Email Address**: Replace `gsoto82@gmail.com` with your actual contact email.
- **API Endpoints**: Customize the API endpoints section based on the actual endpoints provided by your application.
- **Testing**: The test descriptions should reflect the actual tests present in your project.

Deployment
Docker
You can build and run the application using Docker:****

docker build -t gs0816 .

docker run -p 8080:8080 gs0816

Open your browser and go to http://localhost:8080.

### Summary

This `README.md` provides a comprehensive guide to setting up, running, and using the Tool Rental application. It is designed to help developers and users understand the project quickly and efficiently.



Here is a sample README.md file for the Tool Rental project. This file provides an overview of the project, instructions on how to set it up, run it, and includes details on how the code is organized.

 