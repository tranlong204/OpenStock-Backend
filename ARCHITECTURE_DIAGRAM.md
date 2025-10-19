# OpenStock Backend - Architecture Diagram

## 📁 Project Structure Overview

```
OpenStock-Backend/
│
├── 📄 Configuration Files
│   ├── pom.xml                    # Maven dependencies & build config
│   ├── .env                       # Environment variables (JWT_SECRET, API keys)
│   ├── docker-compose.yml         # MongoDB container setup
│   ├── start.sh                   # Backend startup script
│   └── stop.sh                    # Backend shutdown script
│
├── 📂 src/main/
│   ├── java/com/opendevsociety/openstock/
│   │   │
│   │   ├── 🚀 OpenstockBackendApplication.java    # Spring Boot Main Entry Point
│   │   │
│   │   ├── 📡 controller/                         # REST API Endpoints Layer
│   │   │   ├── AuthController.java                # /api/auth/** (signup, signin, signout)
│   │   │   ├── StockController.java               # /api/stocks/** (search, price)
│   │   │   ├── NewsController.java                # /api/news (market news)
│   │   │   └── WatchlistController.java           # /api/watchlist/** (CRUD operations)
│   │   │
│   │   ├── 💼 service/                            # Business Logic Layer
│   │   │   ├── AuthService.java                   # User authentication & registration
│   │   │   ├── WatchlistService.java              # Watchlist management
│   │   │   ├── FinnhubService.java                # External API integration (stocks, news)
│   │   │   ├── EmailService.java                  # Email notifications
│   │   │   └── ScheduledNewsService.java          # Scheduled tasks (Quartz)
│   │   │
│   │   ├── 🗄️ repository/                         # Data Access Layer (Spring Data MongoDB)
│   │   │   ├── UserRepository.java                # User CRUD operations
│   │   │   ├── WatchlistRepository.java           # Watchlist CRUD operations
│   │   │   └── AlertRepository.java               # Alert CRUD operations
│   │   │
│   │   ├── 🏗️ entity/                             # MongoDB Document Models
│   │   │   ├── User.java                          # User document (@Document)
│   │   │   ├── WatchlistItem.java                 # Watchlist document
│   │   │   └── Alert.java                         # Alert document
│   │   │
│   │   ├── 📦 dto/                                # Data Transfer Objects
│   │   │   ├── SignUpRequest.java                 # Sign-up request payload
│   │   │   ├── SignInRequest.java                 # Sign-in request payload
│   │   │   ├── AuthResponse.java                  # Auth response (token, user)
│   │   │   ├── UserDto.java                       # User data transfer
│   │   │   ├── StockDto.java                      # Stock search result
│   │   │   ├── StockPriceDto.java                 # Stock price data
│   │   │   ├── NewsArticleDto.java                # News article data
│   │   │   └── WatchlistItemDto.java              # Watchlist item data
│   │   │
│   │   ├── 🔐 security/                           # Security Components
│   │   │   ├── JwtAuthenticationFilter.java       # JWT token validation filter
│   │   │   └── JwtAuthenticationEntryPoint.java   # Unauthorized access handler
│   │   │
│   │   ├── ⚙️ config/                             # Spring Configuration
│   │   │   ├── SecurityConfig.java                # Spring Security & CORS config
│   │   │   └── WebClientConfig.java               # HTTP client for external APIs
│   │   │
│   │   └── 🛠️ util/                               # Utility Classes
│   │       └── JwtUtil.java                       # JWT token generation & validation
│   │
│   └── resources/
│       └── application.yml                        # Application properties & config
│
└── 📂 src/test/
    └── java/com/opendevsociety/openstock/
        └── controller/
            └── AuthControllerTest.java            # Unit tests for Auth
```

---

## 🏗️ Layered Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                              │
│                   (Next.js Frontend / Mobile)                    │
└─────────────────────────────────────────────────────────────────┘
                              ↓ HTTP/REST
┌─────────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                           │
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   Auth       │  │   Stock      │  │  Watchlist   │          │
│  │ Controller   │  │ Controller   │  │ Controller   │  ...     │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│         REST API Endpoints (@RestController)                     │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      SECURITY LAYER                              │
│                                                                   │
│  ┌────────────────────────────────────────────────────────┐     │
│  │  Spring Security Filter Chain                          │     │
│  │  ├── CORS Filter                                       │     │
│  │  ├── JWT Authentication Filter (Custom)                │     │
│  │  └── Authorization Filter                              │     │
│  └────────────────────────────────────────────────────────┘     │
│                                                                   │
│  ┌────────────────────┐    ┌─────────────────────────────┐     │
│  │  JwtUtil           │    │  SecurityConfig              │     │
│  │  (Token Gen/Valid) │    │  (Security Rules & CORS)     │     │
│  └────────────────────┘    └─────────────────────────────┘     │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      BUSINESS LOGIC LAYER                        │
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   Auth       │  │  Watchlist   │  │  Finnhub     │          │
│  │  Service     │  │  Service     │  │  Service     │  ...     │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│         Business Rules & Domain Logic (@Service)                 │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                    DATA ACCESS LAYER                             │
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   User       │  │  Watchlist   │  │    Alert     │          │
│  │ Repository   │  │ Repository   │  │ Repository   │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
│      Spring Data MongoDB (@Repository)                           │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                      DATABASE LAYER                              │
│                                                                   │
│                   MongoDB (NoSQL Database)                       │
│                                                                   │
│         Collections: users, watchlist, alerts                    │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                    EXTERNAL SERVICES                             │
│                                                                   │
│  ┌────────────────┐    ┌─────────────────┐                      │
│  │  Finnhub API   │    │  Email Service  │                      │
│  │  (WebClient)   │    │  (SMTP/Gmail)   │                      │
│  └────────────────┘    └─────────────────┘                      │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Request Flow Example: User Sign Up

```
┌─────────┐                                                    ┌──────────┐
│ Client  │                                                    │ MongoDB  │
└────┬────┘                                                    └────┬─────┘
     │                                                              │
     │ 1. POST /api/auth/signup                                    │
     │    {fullName, email, password, ...}                         │
     ├──────────────────────────────────────────────────┐         │
     │                                                   ↓         │
     │                             ┌──────────────────────────┐   │
     │                             │  AuthController          │   │
     │                             │  - Validate request      │   │
     │                             └──────────┬───────────────┘   │
     │                                        │                    │
     │                                        │ 2. Call signUp()   │
     │                             ┌──────────▼───────────────┐   │
     │                             │  AuthService             │   │
     │                             │  - Check if user exists  │   │
     │                             │  - Hash password         │   │
     │                             │  - Create user entity    │   │
     │                             └──────────┬───────────────┘   │
     │                                        │                    │
     │                                        │ 3. save(user)      │
     │                             ┌──────────▼───────────────┐   │
     │                             │  UserRepository          │   │
     │                             │  - Persist to MongoDB    │   │
     │                             └──────────┬───────────────┘   │
     │                                        │                    │
     │                                        ├───────────────────►│
     │                                        │ 4. Insert document │
     │                                        │◄───────────────────┤
     │                                        │ 5. Return user     │
     │                             ┌──────────▼───────────────┐   │
     │                             │  JwtUtil                 │   │
     │                             │  - Generate JWT token    │   │
     │                             └──────────┬───────────────┘   │
     │                                        │                    │
     │                             ┌──────────▼───────────────┐   │
     │                             │  EmailService            │   │
     │                             │  - Send welcome email    │   │
     │                             └──────────┬───────────────┘   │
     │                                        │                    │
     │ 6. Return AuthResponse                 │                    │
     │    {success, token, user}              │                    │
     │◄───────────────────────────────────────┘                    │
     │                                                              │
```

---

## 🔐 Security Architecture

```
┌────────────────────────────────────────────────────────────────┐
│                    SPRING SECURITY FLOW                         │
└────────────────────────────────────────────────────────────────┘

  Incoming Request
        │
        ↓
  ┌─────────────────┐
  │  CORS Filter    │ ← Allow origins from frontend
  └────────┬────────┘
           │
           ↓
  ┌─────────────────────────────────┐
  │  JwtAuthenticationFilter        │
  │                                  │
  │  1. Extract JWT from header     │
  │     Authorization: Bearer <jwt> │
  │                                  │
  │  2. Validate token (JwtUtil)    │
  │     - Signature verification    │
  │     - Expiration check          │
  │     - Claims validation         │
  │                                  │
  │  3. Load user from email        │
  │     (AuthService)                │
  │                                  │
  │  4. Set SecurityContext         │
  │     with Authentication         │
  └────────┬────────────────────────┘
           │
           ↓
  ┌─────────────────────────────────┐
  │  Authorization Filter            │
  │                                  │
  │  Public endpoints:               │
  │    - /api/auth/**                │
  │    - /api/stocks/search          │
  │    - /api/news/**                │
  │                                  │
  │  Protected endpoints:            │
  │    - /api/watchlist/**           │
  │    - /api/user/**                │
  └────────┬────────────────────────┘
           │
           ↓
  ┌─────────────────┐
  │   Controller    │
  └─────────────────┘
```

### JWT Token Structure

```
Header:
{
  "alg": "HS512",
  "typ": "JWT"
}

Payload:
{
  "sub": "user@example.com",
  "iat": 1634567890,
  "exp": 1634654290
}

Signature:
HMACSHA512(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  JWT_SECRET
)
```

---

## 📊 Database Schema (MongoDB Collections)

### **users** Collection
```json
{
  "_id": "ObjectId",
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "$2a$10$hashed...",
  "country": "US",
  "investmentGoals": "Growth",
  "riskTolerance": "Medium",
  "preferredIndustry": "Technology",
  "createdAt": "ISODate"
}
```

### **watchlist** Collection
```json
{
  "_id": "ObjectId",
  "userId": "ObjectId (ref: users)",
  "symbol": "AAPL",
  "company": "Apple Inc.",
  "addedAt": "ISODate"
}
```

### **alerts** Collection
```json
{
  "_id": "ObjectId",
  "userId": "ObjectId (ref: users)",
  "symbol": "TSLA",
  "company": "Tesla Inc.",
  "alertName": "Price Alert",
  "alertType": "upper",
  "threshold": 900.00,
  "createdAt": "ISODate"
}
```

---

## 🌐 API Endpoints Overview

### Authentication (`/api/auth`)
```
POST   /api/auth/signup   - Create new user account
POST   /api/auth/signin   - Authenticate & get JWT token
POST   /api/auth/signout  - Sign out (client-side token removal)
```

### Stocks (`/api/stocks`)
```
GET    /api/stocks/search?query={symbol}  - Search stocks
GET    /api/stocks/price/{symbol}         - Get stock price
```

### News (`/api/news`)
```
GET    /api/news?symbols={symbols}  - Get market news
```

### Watchlist (`/api/watchlist`) 🔒 Protected
```
GET    /api/watchlist           - Get user's watchlist
POST   /api/watchlist           - Add stock to watchlist
DELETE /api/watchlist/{symbol}  - Remove from watchlist
```

### User (`/api/user`) 🔒 Protected
```
GET    /api/user/profile  - Get current user profile
```

---

## 🔌 External API Integration

```
┌──────────────────────────────────────────────────────────┐
│              FinnhubService (WebClient)                   │
├──────────────────────────────────────────────────────────┤
│                                                           │
│  Finnhub API Endpoints:                                  │
│                                                           │
│  1. Stock Search                                         │
│     GET /search?q={query}                                │
│     → Returns: List of matching stocks                   │
│                                                           │
│  2. Stock Quote                                          │
│     GET /quote?symbol={symbol}                           │
│     → Returns: Real-time price, volume, change           │
│                                                           │
│  3. Company News                                         │
│     GET /company-news?symbol={symbol}&from={date}&to={d} │
│     → Returns: Company-specific news articles            │
│                                                           │
│  4. General News                                         │
│     GET /news?category=general                           │
│     → Returns: Market-wide news articles                 │
│                                                           │
└──────────────────────────────────────────────────────────┘
```

---

## ⚙️ Configuration & Environment

### application.yml
```yaml
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/openstock}
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}

app:
  jwt:
    secret: ${JWT_SECRET}
    expiration: 86400000  # 24 hours
  finnhub:
    api-key: ${FINNHUB_API_KEY}
    base-url: https://finnhub.io/api/v1
```

### .env (Not committed to Git)
```bash
JWT_SECRET=<512-bit-base64-secret>
FINNHUB_API_KEY=<your-api-key>
MONGODB_URI=mongodb://localhost:27017/openstock
MAIL_USERNAME=<gmail-address>
MAIL_PASSWORD=<app-password>
```

---

## 🔄 Scheduled Tasks (Quartz)

```
┌──────────────────────────────────────────────────────────┐
│         ScheduledNewsService (@Scheduled)                 │
├──────────────────────────────────────────────────────────┤
│                                                           │
│  @Scheduled(cron = "0 0 */6 * * *")                      │
│  void fetchAndCacheNews()                                │
│                                                           │
│  - Runs every 6 hours                                    │
│  - Fetches latest market news                            │
│  - Caches for quick retrieval                            │
│  - Reduces API calls to Finnhub                          │
│                                                           │
└──────────────────────────────────────────────────────────┘
```

---

## 🛠️ Technology Stack

| Layer              | Technology                    |
|--------------------|-------------------------------|
| **Framework**      | Spring Boot 3.3.5            |
| **Language**       | Java 21                      |
| **Build Tool**     | Maven                        |
| **Database**       | MongoDB (NoSQL)              |
| **ORM**            | Spring Data MongoDB          |
| **Security**       | Spring Security + JWT        |
| **HTTP Client**    | Spring WebFlux WebClient     |
| **Scheduler**      | Quartz (Spring Integration)  |
| **Email**          | Spring Mail + JavaMail       |
| **Validation**     | Jakarta Bean Validation      |
| **Logging**        | Logback (SLF4J)              |
| **Testing**        | JUnit 5, Mockito             |
| **Container**      | Docker (MongoDB)             |

---

## 📦 Key Dependencies (pom.xml)

```xml
<!-- Core Spring Boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Spring Data MongoDB -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>

<!-- WebClient for HTTP -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

<!-- Quartz Scheduler -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>

<!-- Email -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

<!-- Bean Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

## 🚀 Deployment & Startup

### Using start.sh
```bash
#!/bin/bash
# Load environment variables from .env
set -a
source .env
set +a

# Start Spring Boot application
java -jar target/openstock-backend-0.1.0.jar
```

### Docker Compose (MongoDB)
```yaml
version: '3.8'
services:
  mongodb:
    image: mongo:7.0
    container_name: openstock-mongodb
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db

volumes:
  mongodb_data:
```

---

## 📈 Design Patterns Used

| Pattern                  | Implementation                           |
|--------------------------|------------------------------------------|
| **Layered Architecture** | Controller → Service → Repository        |
| **DTO Pattern**          | Separate request/response objects        |
| **Repository Pattern**   | Spring Data MongoDB repositories         |
| **Dependency Injection** | Spring's @Autowired, constructor DI      |
| **Filter Chain**         | Spring Security filter chain             |
| **Builder Pattern**      | JWT token building, Entity creation      |
| **Singleton**            | Spring beans (default scope)            |
| **Factory Pattern**      | Spring bean factory                      |
| **Template Method**      | Spring's JpaRepository methods           |
| **Strategy Pattern**     | Password encoding (BCrypt)               |

---

## 🔒 Security Best Practices

✅ **Implemented:**
- JWT-based stateless authentication
- Password hashing with BCrypt
- CORS configuration for frontend access
- Input validation with Jakarta Validation
- Environment variable for sensitive data
- HTTPS-ready (production deployment)
- SQL Injection prevention (NoSQL MongoDB)
- XSS protection (JSON serialization)

---

## 📝 Notes

1. **Stateless Authentication**: JWT tokens eliminate server-side session storage
2. **NoSQL Database**: MongoDB for flexible schema and scalability
3. **Reactive HTTP Client**: WebClient for non-blocking external API calls
4. **Scheduled Tasks**: Quartz for periodic news fetching
5. **Email Notifications**: Welcome emails on user registration
6. **API Rate Limiting**: Handled by Finnhub (free tier: 60 calls/min)

---

**Generated:** October 19, 2025  
**Version:** 0.1.0  
**Java Version:** 21  
**Spring Boot Version:** 3.3.5

