# WishList API

A RESTful API for managing e-commerce customer wishlists, built as a learning project to practice software engineering best practices.

## 📌 About This Project

**WishList API** is an educational project designed to explore and implement industry-standard software engineering practices while building a practical application. Although this is not intended for production use, it adheres to professional development standards and architectural patterns.

### Learning Objectives

This project demonstrates expertise in:
- **Clean Architecture** — Separation of concerns across domain, application, and infrastructure layers
- **SOLID Principles** — Single responsibility, dependency inversion, and abstraction-based design
- **Domain-Driven Design** — Value objects and entity modeling
- **Repository Pattern** — Data access abstraction and flexible persistence
- **Automated Testing** — Test-driven development practices
- **Database Design** — MongoDB and PostgreSQL integration patterns
- **Spring Boot** — Modern Java web framework and dependency injection

## 🚀 Quick Start

### Prerequisites
- Java 25
- Maven 3.8+
- Docker & Docker Compose

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd wishlist
   ```

2. **Start MongoDB**
   ```bash
   docker-compose up -d
   ```

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The API will be available at `http://localhost:8080`

## 📚 Architecture

This project follows **Clean Architecture** principles, organizing code into distinct layers:

```
├── domain/                 # Business logic and core concepts
│   ├── entity/            # Core domain entities (WishList)
│   ├── vo/                # Value objects (ProductId)
│   └── repository/        # Repository abstractions
├── application/           # Use case orchestration
│   └── usecase/           # Business workflows
├── infrastructure/        # Technical implementations
│   └── repository/        # Database adapters and persistence
└── interfaces/            # External interactions
    ├── api/              # REST controllers
    └── config/           # Spring configuration
```

### Design Patterns

- **Repository Pattern** — Abstracts data access, allowing flexible persistence implementations
- **Mapper Pattern** — Separates domain entities from persistence models using MapStruct
- **Value Objects** — Encapsulates domain concepts with built-in validation
- **Dependency Injection** — Spring manages component lifecycle and wiring

## 🔌 API Endpoints

### Add Product to Wishlist
```
POST /api/wishlists/{customerId}/products/{productId}
```

**Response:** `201 Created`

**Example:**
```bash
curl -X POST http://localhost:8080/api/wishlists/customer123/products/product456
```

**Behavior:**
- Creates a new wishlist if one doesn't exist for the customer
- Adds the product to the wishlist
- Returns 201 Created on success

## 🧪 Testing

Run all tests:
```bash
./mvnw test
```

Run a specific test class:
```bash
./mvnw test -Dtest=WishlistApplicationTests
```

## 🛠 Technology Stack

| Layer | Technology |
|-------|------------|
| Runtime | Java 25 |
| Framework | Spring Boot 4.0.8 |
| Build Tool | Maven |
| Database | MongoDB (Spring Data MongoDB) |
| Mapping | MapStruct |
| Testing | Spring Boot Test |

## 📋 Project Status & Future Enhancements

**Current Implementation:**
- Basic wishlist creation and product addition
- MongoDB persistence with clean repository abstraction

**Planned Enhancements:**
- ✅ Maximum wishlist size validation
- ✅ Duplicate product detection
- ✅ PostgreSQL adapter implementation
- ✅ Comprehensive test coverage (unit, integration)
- ✅ API documentation (OpenAPI/Swagger)
- ✅ Error handling and validation layers
- ✅ Authentication and authorization

> Note: See `CLAUDE.md` for detailed developer guidance and architecture decisions.

## 💡 Development Practices

This project demonstrates:

✓ **Clear separation of concerns** — Each layer has a specific responsibility  
✓ **Dependency inversion** — Domain layer depends on abstractions, not implementations  
✓ **Value object validation** — Business rules enforced at object creation  
✓ **Testability** — Repository abstraction enables easy mocking and testing  
✓ **Maintainability** — Clean code structure facilitates future enhancements  
✓ **Documentation** — Code is self-documenting through clear naming and structure  

## 📖 For Developers

See `CLAUDE.md` for:
- Architecture deep dive
- Common development commands
- Database setup instructions
- Important implementation notes

## 📝 License

Educational project — Feel free to use as a learning reference.

## 👤 Author

Created as a learning exercise in advanced software engineering practices.

---

**Status:** 🎓 Educational Project | Not intended for production use
