# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**WishList API** — A Spring Boot REST API for managing customer wishlists with product management. Uses clean architecture with MongoDB for persistence.

**Tech Stack:**
- Java 25
- Spring Boot 4.0.8
- MongoDB (via Spring Data MongoDB)
- MapStruct for object mapping
- Maven for build management

## Architecture

The project follows **clean architecture** with clear separation of concerns across layers:

```
interfaces/
  ├── api/controller/        # REST controllers (HTTP entry points)
  └── config/                # Spring configuration & DI setup

application/
  └── usecase/               # Business logic orchestration

domain/
  ├── entity/                # Core domain entities (WishList)
  ├── vo/                    # Value objects (ProductId)
  └── repository/            # Repository interfaces (abstractions)

infrastructure/
  └── repository/            # Repository implementations using MongoDB
      └── persistence/       # Database mappers & documents
```

**Key Patterns:**
- **Repository Pattern**: Domain defines interfaces; infrastructure provides MongoDB implementation
- **Mapper Pattern**: WishListMapper converts between domain entities (WishList with ProductId value objects) and database documents (WishListDocument with String IDs)
- **Value Objects**: ProductId is a value object with validation in constructor
- **Dependency Injection**: Spring manages all dependencies; repositories are injected into use cases

## Common Commands

**Build the project:**
```bash
./mvnw clean install
```

**Run the application:**
```bash
./mvnw spring-boot:run
```

**Run tests:**
```bash
./mvnw test
```

**Run a specific test:**
```bash
./mvnw test -Dtest=WishlistApplicationTests
```

**Compile only (no tests):**
```bash
./mvnw clean compile
```

## Database Setup for development

MongoDB is configured via Docker Compose (no authentication required):

```bash
# Start MongoDB
docker-compose up -d

# Stop MongoDB
docker-compose down
```