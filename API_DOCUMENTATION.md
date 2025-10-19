# 📋 **OpenStock Spring Boot API Documentation**

## 🌐 **Base URL**
```
http://localhost:8080
```

---

## 🔐 **Authentication APIs**

### **POST** `/api/auth/signup`
**Description**: Register a new user account  
**Authentication**: Not required  
**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```
**Example URI**: `POST http://localhost:8080/api/auth/signup`

**cURL Example**:
```bash
curl -X POST "http://localhost:8080/api/auth/signup" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

**Response Example**:
```json
{
  "success": true,
  "message": "User registered successfully",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "user123",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```

---

### **POST** `/api/auth/signin`
**Description**: Login with existing credentials  
**Authentication**: Not required  
**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
**Example URI**: `POST http://localhost:8080/api/auth/signin`

**cURL Example**:
```bash
curl -X POST "http://localhost:8080/api/auth/signin" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

**Response Example**:
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "user123",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```

---

### **POST** `/api/auth/signout`
**Description**: Logout (client-side token removal)  
**Authentication**: Not required  
**Example URI**: `POST http://localhost:8080/api/auth/signout`

**cURL Example**:
```bash
curl -X POST "http://localhost:8080/api/auth/signout"
```

**Response Example**:
```json
"Signed out successfully"
```

---

## 📈 **Stock APIs**

### **GET** `/api/stocks/search`
**Description**: Search for stocks by symbol or company name  
**Authentication**: Optional (Bearer token for personalized results)  
**Query Parameters**:
- `query` (optional): Search term (e.g., "AAPL", "Apple")

**Example URIs**:
- `GET http://localhost:8080/api/stocks/search?query=AAPL`
- `GET http://localhost:8080/api/stocks/search?query=Apple`
- `GET http://localhost:8080/api/stocks/search` (returns popular stocks)

**cURL Examples**:
```bash
# Search for Apple stock
curl "http://localhost:8080/api/stocks/search?query=AAPL"

# Search for Apple company
curl "http://localhost:8080/api/stocks/search?query=Apple"

# Get popular stocks
curl "http://localhost:8080/api/stocks/search"

# With authentication (for personalized results)
curl -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/stocks/search?query=AAPL"
```

**Response Example**:
```json
[
  {
    "symbol": "AAPL",
    "name": "Apple Inc.",
    "exchange": "US",
    "type": "Stock",
    "inWatchlist": false
  },
  {
    "symbol": "MSFT",
    "name": "Microsoft Corporation",
    "exchange": "US",
    "type": "Stock",
    "inWatchlist": true
  }
]
```

### **GET** `/api/stocks/price/{symbol}`
**Description**: Get current stock price and market data for a specific symbol  
**Authentication**: Not required  
**Path Parameters**:
- `symbol`: Stock symbol (e.g., "AAPL", "MSFT", "TSLA")

**Example URIs**:
- `GET http://localhost:8080/api/stocks/price/AAPL`
- `GET http://localhost:8080/api/stocks/price/MSFT`
- `GET http://localhost:8080/api/stocks/price/TSLA`

**cURL Examples**:
```bash
# Get Apple stock price
curl "http://localhost:8080/api/stocks/price/AAPL"

# Get Microsoft stock price
curl "http://localhost:8080/api/stocks/price/MSFT"

# Get Tesla stock price
curl "http://localhost:8080/api/stocks/price/TSLA"
```

**Response Example**:
```json
{
  "symbol": "AAPL",
  "name": "Apple Inc.",
  "currentPrice": 531.8,
  "openPrice": 531.56,
  "highPrice": 533.05,
  "lowPrice": 528.79,
  "previousClose": 528.3,
  "volume": 1550159,
  "timestamp": "2025-10-18T17:25:19.409679",
  "exchange": "NASDAQ",
  "currency": "USD",
  "priceChange": 3.5,
  "priceChangePercent": 0.6625023660798789
}
```

**Response Fields**:
- `symbol`: Stock symbol
- `name`: Company name
- `currentPrice`: Current stock price
- `openPrice`: Opening price for the day
- `highPrice`: Highest price for the day
- `lowPrice`: Lowest price for the day
- `previousClose`: Previous day's closing price
- `volume`: Trading volume
- `timestamp`: Last updated timestamp
- `exchange`: Stock exchange (e.g., "NASDAQ")
- `currency`: Currency code (e.g., "USD")
- `priceChange`: Price change from previous close
- `priceChangePercent`: Percentage change from previous close

---

## 📰 **News APIs**

### **GET** `/api/news`
**Description**: Get news articles (personalized if authenticated)  
**Authentication**: Optional (Bearer token for watchlist-based news)  
**Example URI**: `GET http://localhost:8080/api/news`

**cURL Examples**:
```bash
# Get general news
curl "http://localhost:8080/api/news"

# Get personalized news based on watchlist
curl -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/news"
```

**Response Example**:
```json
[
  {
    "id": "news_123",
    "title": "Apple Reports Strong Q4 Earnings",
    "summary": "Apple Inc. reported better than expected earnings for the fourth quarter...",
    "url": "https://example.com/news/apple-earnings",
    "publishedAt": "2025-01-15T10:30:00Z",
    "source": "Financial News"
  },
  {
    "id": "news_124",
    "title": "Microsoft Announces New AI Features",
    "summary": "Microsoft Corporation unveiled new artificial intelligence capabilities...",
    "url": "https://example.com/news/microsoft-ai",
    "publishedAt": "2025-01-15T09:15:00Z",
    "source": "Tech News"
  }
]
```

---

### **GET** `/api/news/symbols`
**Description**: Get news for specific stock symbols  
**Authentication**: Not required  
**Query Parameters**:
- `symbols`: Comma-separated list of stock symbols

**Example URIs**:
- `GET http://localhost:8080/api/news/symbols?symbols=AAPL,MSFT,GOOGL`
- `GET http://localhost:8080/api/news/symbols?symbols=TSLA`

**cURL Examples**:
```bash
# Get news for multiple stocks
curl "http://localhost:8080/api/news/symbols?symbols=AAPL,MSFT,GOOGL"

# Get news for Tesla only
curl "http://localhost:8080/api/news/symbols?symbols=TSLA"
```

**Response Example**:
```json
[
  {
    "id": "news_125",
    "title": "Tesla Stock Surges on Delivery Report",
    "summary": "Tesla Inc. shares jumped after reporting record vehicle deliveries...",
    "url": "https://example.com/news/tesla-deliveries",
    "publishedAt": "2025-01-15T08:45:00Z",
    "source": "Market News"
  }
]
```

---

## ⭐ **Watchlist APIs**

### **GET** `/api/watchlist`
**Description**: Get user's watchlist  
**Authentication**: Required (Bearer token)  
**Example URI**: `GET http://localhost:8080/api/watchlist`

**cURL Example**:
```bash
curl -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/watchlist"
```

**Response Example**:
```json
[
  {
    "id": "watchlist_1",
    "symbol": "AAPL",
    "company": "Apple Inc.",
    "addedAt": "2025-01-15T10:30:00Z"
  },
  {
    "id": "watchlist_2",
    "symbol": "MSFT",
    "company": "Microsoft Corporation",
    "addedAt": "2025-01-14T15:20:00Z"
  }
]
```

---

### **POST** `/api/watchlist`
**Description**: Add stock to watchlist  
**Authentication**: Required (Bearer token)  
**Query Parameters**:
- `symbol`: Stock symbol (e.g., "AAPL")
- `company`: Company name (e.g., "Apple Inc.")

**Example URI**: `POST http://localhost:8080/api/watchlist?symbol=AAPL&company=Apple Inc.`

**cURL Example**:
```bash
curl -X POST \
  -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/watchlist?symbol=AAPL&company=Apple Inc."
```

**Response Example**:
```json
{
  "id": "watchlist_3",
  "symbol": "AAPL",
  "company": "Apple Inc.",
  "addedAt": "2025-01-15T11:00:00Z"
}
```

---

### **DELETE** `/api/watchlist/{symbol}`
**Description**: Remove stock from watchlist  
**Authentication**: Required (Bearer token)  
**Path Parameters**:
- `symbol`: Stock symbol to remove

**Example URIs**:
- `DELETE http://localhost:8080/api/watchlist/AAPL`
- `DELETE http://localhost:8080/api/watchlist/MSFT`

**cURL Examples**:
```bash
# Remove Apple from watchlist
curl -X DELETE \
  -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/watchlist/AAPL"

# Remove Microsoft from watchlist
curl -X DELETE \
  -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/watchlist/MSFT"
```

**Response Example**:
```json
"Removed from watchlist"
```

---

### **GET** `/api/watchlist/check/{symbol}`
**Description**: Check if stock is in user's watchlist  
**Authentication**: Required (Bearer token)  
**Path Parameters**:
- `symbol`: Stock symbol to check

**Example URIs**:
- `GET http://localhost:8080/api/watchlist/check/AAPL`
- `GET http://localhost:8080/api/watchlist/check/TSLA`

**cURL Examples**:
```bash
# Check if Apple is in watchlist
curl -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/watchlist/check/AAPL"

# Check if Tesla is in watchlist
curl -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/watchlist/check/TSLA"
```

**Response Example**:
```json
true
```

---

## 🔧 **System/Monitoring APIs**

### **GET** `/actuator/health`
**Description**: Application health check  
**Authentication**: Not required  
**Example URI**: `GET http://localhost:8080/actuator/health`

**cURL Example**:
```bash
curl "http://localhost:8080/actuator/health"
```

**Response Example**:
```json
{
  "status": "UP",
  "components": {
    "mongo": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

---

### **GET** `/actuator/info`
**Description**: Application information  
**Authentication**: Not required  
**Example URI**: `GET http://localhost:8080/actuator/info`

**cURL Example**:
```bash
curl "http://localhost:8080/actuator/info"
```

**Response Example**:
```json
{
  "app": {
    "name": "openstock-backend",
    "version": "0.1.0"
  },
  "java": {
    "version": "21.0.6"
  }
}
```

---

### **GET** `/actuator/metrics`
**Description**: Application metrics  
**Authentication**: Not required  
**Example URI**: `GET http://localhost:8080/actuator/metrics`

**cURL Example**:
```bash
curl "http://localhost:8080/actuator/metrics"
```

**Response Example**:
```json
{
  "names": [
    "jvm.memory.used",
    "jvm.memory.max",
    "http.server.requests",
    "process.uptime"
  ]
}
```

---

## 🔒 **Authentication & Security**

### **JWT Token Usage**
- **Format**: `Bearer <jwt_token>`
- **Header**: `Authorization: Bearer <your_jwt_token>`
- **Expiration**: 24 hours (86400000 ms)
- **Required for**: All watchlist endpoints

### **Token Example**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzM3MTI5NjAwLCJleHAiOjE3MzcyMTYwMDB9.signature
```

---

## 📊 **Error Responses**

### **Authentication Error**
```json
{
  "error": "Unauthorized",
  "message": "Access denied"
}
```

### **Validation Error**
```json
{
  "error": "Validation Error",
  "message": "Email is required",
  "field": "email"
}
```

### **Not Found Error**
```json
{
  "error": "Not Found",
  "message": "User not found"
}
```

---

## 🧪 **Complete Testing Workflow**

### **1. Register a new user**
```bash
curl -X POST "http://localhost:8080/api/auth/signup" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### **2. Login and get token**
```bash
curl -X POST "http://localhost:8080/api/auth/signin" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### **3. Search for stocks**
```bash
curl "http://localhost:8080/api/stocks/search?query=AAPL"
```

### **4. Add stock to watchlist**
```bash
curl -X POST \
  -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/watchlist?symbol=AAPL&company=Apple Inc."
```

### **5. Get personalized news**
```bash
curl -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/news"
```

### **6. Check watchlist**
```bash
curl -H "Authorization: Bearer <your_jwt_token>" \
  "http://localhost:8080/api/watchlist"
```

---

## 📝 **Notes**

- **CORS**: All endpoints support CORS with `origins = "*"`
- **Content-Type**: Use `application/json` for POST requests
- **Base URL**: Change `localhost:8080` to your actual server URL in production
- **Rate Limiting**: Consider implementing rate limiting for production use
- **HTTPS**: Use HTTPS in production environments

---

## 🚀 **Quick Start Commands**

```bash
# Start the application
cd /Users/longtran/source/stock/OpenStock-spring-boot
mvn spring-boot:run

# Test health endpoint
curl http://localhost:8080/actuator/health

# Test stock search
curl "http://localhost:8080/api/stocks/search?query=AAPL"

# Test news endpoint
curl http://localhost:8080/api/news
```

---

*Last updated: January 18, 2025*
