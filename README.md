# OpenStock Backend - Spring Boot

This is the Spring Boot backend implementation of the OpenStock application, converted from the original Node.js/Next.js implementation.

## Features

- **Authentication**: JWT-based authentication with Spring Security
- **User Management**: User registration, login, and profile management
- **Stock Data**: Integration with Finnhub API for stock search and data
- **Watchlist**: Add/remove stocks from user watchlists
- **News**: Fetch market news based on user watchlists or general market news
- **Email Notifications**: Welcome emails and daily news summaries
- **Scheduled Tasks**: Automated daily news email delivery
- **MongoDB**: Document-based data storage
- **REST API**: Clean REST endpoints for frontend integration

## Technology Stack

- **Java 21**
- **Spring Boot 3.3.0**
- **Spring Security** (JWT Authentication)
- **Spring Data MongoDB**
- **Spring WebFlux** (Reactive HTTP Client)
- **MongoDB**
- **Spring Mail** (Email notifications)
- **Spring Quartz** (Scheduling)
- **Maven** (Dependency Management)

## API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/signin` - User login
- `POST /api/auth/signout` - User logout

### Stocks
- `GET /api/stocks/search?query={query}` - Search stocks

### Watchlist
- `GET /api/watchlist` - Get user watchlist
- `POST /api/watchlist?symbol={symbol}&company={company}` - Add to watchlist
- `DELETE /api/watchlist/{symbol}` - Remove from watchlist
- `GET /api/watchlist/check/{symbol}` - Check if stock is in watchlist

### News
- `GET /api/news` - Get news (user-specific if authenticated)
- `GET /api/news/symbols?symbols={symbols}` - Get news for specific symbols

## Configuration

### Environment Variables

```bash
# MongoDB
MONGODB_URI=mongodb://localhost:27017/openstock

# JWT
JWT_SECRET=your-secret-key-here-change-in-production
JWT_EXPIRATION=86400000

# Finnhub API
FINNHUB_API_KEY=your-finnhub-api-key

# Email (Gmail SMTP)
NODEMAILER_EMAIL=your-gmail-email
NODEMAILER_PASSWORD=your-gmail-app-password
```

### Application Properties

The application uses `application.yml` for configuration. Key settings:

```yaml
server:
  port: 8080

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/openstock
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${NODEMAILER_EMAIL:}
    password: ${NODEMAILER_PASSWORD:}

app:
  jwt:
    secret: ${JWT_SECRET:your-secret-key-here-change-in-production}
    expiration: 86400000
  finnhub:
    api-key: ${FINNHUB_API_KEY:}
    base-url: https://finnhub.io/api/v1
```

## Running the Application

### Prerequisites

1. **Java 21** or higher
2. **MongoDB** running on localhost:27017
3. **Maven** for dependency management

### Local Development

1. Clone the repository
2. Set up environment variables
3. Run MongoDB locally
4. Execute the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Docker

```bash
# Build the application
mvn clean package

# Build Docker image
docker build -t openstock-backend .

# Run with Docker Compose
docker-compose up
```

## Database Schema

### Users Collection
```json
{
  "_id": "ObjectId",
  "email": "user@example.com",
  "name": "John Doe",
  "password": "hashed_password",
  "country": "United States",
  "investmentGoals": "Growth",
  "riskTolerance": "Medium",
  "preferredIndustry": "Technology",
  "createdAt": "ISODate",
  "updatedAt": "ISODate",
  "emailVerified": false,
  "active": true
}
```

### Watchlist Collection
```json
{
  "_id": "ObjectId",
  "userId": "user_id",
  "symbol": "AAPL",
  "company": "Apple Inc.",
  "addedAt": "ISODate"
}
```

### Alerts Collection
```json
{
  "_id": "ObjectId",
  "userId": "user_id",
  "symbol": "AAPL",
  "company": "Apple Inc.",
  "alertName": "Price Alert",
  "alertType": "UPPER",
  "threshold": 150.0,
  "currentPrice": 145.0,
  "changePercent": 2.5,
  "active": true,
  "createdAt": "ISODate",
  "updatedAt": "ISODate"
}
```

## Migration from Node.js/Next.js

This Spring Boot implementation provides equivalent functionality to the original Node.js/Next.js backend:

### Key Differences

1. **Authentication**: JWT tokens instead of Better Auth sessions
2. **Database**: Spring Data MongoDB instead of Mongoose
3. **HTTP Client**: WebFlux instead of fetch API
4. **Scheduling**: Spring Quartz instead of Inngest
5. **Email**: Spring Mail instead of Nodemailer
6. **Security**: Spring Security instead of Better Auth

### Equivalent Features

- ✅ User registration and authentication
- ✅ Stock search via Finnhub API
- ✅ Watchlist management
- ✅ News fetching and personalization
- ✅ Email notifications (welcome and daily news)
- ✅ Scheduled daily news emails
- ✅ CORS support for frontend integration

## Development

### Project Structure

```
src/main/java/com/opendevsociety/openstock/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/            # Data transfer objects
├── entity/         # MongoDB entities
├── repository/     # Data repositories
├── security/      # Security configuration
├── service/       # Business logic services
└── util/          # Utility classes
```

### Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the same license as the original OpenStock project.
