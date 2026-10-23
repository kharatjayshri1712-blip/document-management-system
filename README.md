# Document Management System with AI PDF Summarization

A secure document management application built using Spring Boot, Java, MySQL, JWT authentication, and a Python Flask AI service. The application supports document operations and AI-powered summarization of text and PDF documents.

## Features

- **JWT Authentication** — Secures protected API endpoints.
- **Document Upload** — Upload documents through a REST API.
- **Document Download** — Retrieve uploaded documents.
- **Document Deletion** — Delete documents through the API.
- **File Listing** — Retrieve a list of available files.
- **File Metadata** — Store and retrieve file names, types, sizes, and upload timestamps.
- **AI Text Summarization** — Generate summaries from supplied text.
- **AI PDF Summarization** — Send a PDF filename to a Python Flask service for summarization.
- **Input Validation and Error Handling** — Handle invalid requests, missing PDFs, and AI service failures.
- **Automated Testing** — Test controller behavior using JUnit 5 and MockMvc.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend programming |
| Spring Boot 3.3.0 | REST API development |
| Spring Security | API security |
| JWT | Token-based authentication |
| Spring Data JPA | Database persistence |
| MySQL | File metadata storage |
| Python and Flask | AI summarization service |
| Maven | Dependency management and builds |
| Swagger / OpenAPI | API documentation and testing |
| JUnit 5 and MockMvc | Automated tests |
| IntelliJ IDEA | Development environment |

## Architecture

The application consists of a Spring Boot backend and a separate Python Flask AI service.

1. A client sends requests to the Spring Boot REST API.
2. Spring Security validates authentication for protected endpoints.
3. The backend handles document operations and stores metadata in MySQL.
4. For AI summarization, the backend communicates with the Python Flask service.
5. The summarization result or an appropriate error response is returned to the client.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/ai/summarize` | Summarize supplied text |
| POST | `/ai/summarize-pdf` | Summarize a PDF using its filename |
| GET | `/files` | List available files |
| GET | `/files/metadata` | Retrieve file metadata |

Additional endpoints support authentication and document upload, download, and deletion. Refer to the Swagger UI for their exact paths and request formats.

## Example: Text Summarization

**Request**

`POST /ai/summarize`

```json
{
  "text": "Java is a programming language used to develop applications."
}
```

**Example response**

```json
{
  "summary": "Java is a programming language for application development."
}
```

## Example: PDF Summarization

**Request**

`POST /ai/summarize-pdf`

```json
{
  "fileName": "sample.pdf"
}
```

**Example response**

```json
{
  "fileName": "sample.pdf",
  "summary": "The document discusses its main topics and key points."
}
```

The example summaries above illustrate the response format; actual summaries depend on the input document and AI service.

## Prerequisites

Install the following before running the project:

- Java JDK 17
- Maven, or use the Maven wrapper if included
- MySQL Server
- Python and Flask for the AI service
- IntelliJ IDEA or another Java IDE

## Setup and Configuration

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Configure the MySQL connection in `application.properties` or `application.yml`.
4. Create the database configured for the application.
5. Configure the Python Flask service URL and any required environment variables.
6. Start the Python AI service.
7. Run the Spring Boot application.
8. Open Swagger UI to explore and test the APIs.

**Configuration note:** Keep database passwords, JWT signing secrets, and other credentials out of GitHub. Use environment variables or local configuration files excluded from version control.

## Running Tests

Run the tests from the project root using Maven:

```bash
mvn test
```

The current automated test suite contains 12 tests covering application startup, authentication enforcement, input validation, successful summarization responses, and error handling.

Some controller tests use mocked services to verify responses without requiring a real AI service or PDF file.

## Future Improvements

- Add comprehensive integration tests against the real Python AI service.
- Improve exception handling and structured logging.
- Add pagination and search for documents.
- Integrate and test Amazon S3 document storage.
- Add Docker support.
- Introduce API Gateway and service discovery.
- Explore asynchronous document processing with Kafka.
- Deploy the application to a cloud platform.

## Project Goal

This project demonstrates practical backend development skills, including REST API design, JWT authentication, database persistence, service integration, input validation, exception handling, and automated testing.