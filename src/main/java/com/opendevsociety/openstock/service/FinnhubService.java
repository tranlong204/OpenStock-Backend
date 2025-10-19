package com.opendevsociety.openstock.service;

import com.opendevsociety.openstock.dto.NewsArticleDto;
import com.opendevsociety.openstock.dto.StockDto;
import com.opendevsociety.openstock.dto.StockPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FinnhubService {
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    @Value("${app.finnhub.api-key}")
    private String apiKey;
    
    @Value("${app.finnhub.base-url}")
    private String baseUrl;
    
    private static final List<String> POPULAR_STOCK_SYMBOLS = Arrays.asList(
        "AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "META", "NVDA", "NFLX", "ORCL", "CRM",
        "ADBE", "INTC", "AMD", "PYPL", "UBER", "ZOOM", "SPOT", "SQ", "SHOP", "ROKU"
    );
    
    public List<StockDto> searchStocks(String query) {
        try {
            if (apiKey == null || apiKey.isEmpty()) {
                return getDefaultStocks();
            }
            
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            
            if (query == null || query.trim().isEmpty()) {
                return getPopularStocks(webClient);
            } else {
                return searchStocksByQuery(webClient, query.trim());
            }
            
        } catch (Exception e) {
            return getDefaultStocks();
        }
    }
    
    public List<NewsArticleDto> getNews(List<String> symbols) {
        try {
            if (apiKey == null || apiKey.isEmpty()) {
                return new ArrayList<>();
            }
            
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            
            if (symbols != null && !symbols.isEmpty()) {
                return getCompanyNews(webClient, symbols);
            } else {
                return getGeneralNews(webClient);
            }
            
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    public StockPriceDto getStockPrice(String symbol) {
        try {
            if (apiKey == null || apiKey.isEmpty()) {
                return getMockStockPrice(symbol);
            }
            
            WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();
            
            // Get stock quote
            String quoteUrl = String.format("/quote?symbol=%s&token=%s", symbol, apiKey);
            
            Mono<Object> quoteResponse = webClient.get()
                    .uri(quoteUrl)
                    .retrieve()
                    .bodyToMono(Object.class);
            
            Object quote = quoteResponse.block();
            
            if (quote != null) {
                return parseStockQuote(symbol, quote);
            }
            
            return getMockStockPrice(symbol);
            
        } catch (Exception e) {
            return getMockStockPrice(symbol);
        }
    }
    
    private List<StockDto> getPopularStocks(WebClient webClient) {
        List<String> topSymbols = POPULAR_STOCK_SYMBOLS.subList(0, Math.min(10, POPULAR_STOCK_SYMBOLS.size()));
        
        return topSymbols.stream()
                .map(symbol -> {
                    try {
                        String url = String.format("/stock/profile2?symbol=%s&token=%s", symbol, apiKey);
                        
                        Mono<Object> response = webClient.get()
                                .uri(url)
                                .retrieve()
                                .bodyToMono(Object.class);
                        
                        Object profile = response.block();
                        
                        if (profile != null) {
                            // Extract name from profile response
                            String name = extractNameFromProfile(profile);
                            if (name != null) {
                                return new StockDto(symbol, name, "US", "Stock");
                            }
                        }
                        
                        return new StockDto(symbol, symbol, "US", "Stock");
                        
                    } catch (Exception e) {
                        return new StockDto(symbol, symbol, "US", "Stock");
                    }
                })
                .limit(15)
                .collect(Collectors.toList());
    }
    
    private List<StockDto> searchStocksByQuery(WebClient webClient, String query) {
        try {
            String url = String.format("/search?q=%s&token=%s", query, apiKey);
            
            Mono<Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Object.class);
            
            Object data = response.block();
            
            if (data != null) {
                return parseSearchResults(data);
            }
            
            return new ArrayList<>();
            
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    private List<NewsArticleDto> getCompanyNews(WebClient webClient, List<String> symbols) {
        List<NewsArticleDto> allArticles = new ArrayList<>();
        String dateRange = getDateRange(5);
        
        for (String symbol : symbols) {
            try {
                String url = String.format("/company-news?symbol=%s&from=%s&to=%s&token=%s", 
                    symbol, dateRange.split(",")[0], dateRange.split(",")[1], apiKey);
                
                Mono<Object[]> response = webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(Object[].class);
                
                Object[] articles = response.block();
                
                if (articles != null) {
                    for (Object article : articles) {
                        NewsArticleDto newsArticle = parseNewsArticle(article, true, symbol);
                        if (newsArticle != null) {
                            allArticles.add(newsArticle);
                        }
                    }
                }
                
            } catch (Exception e) {
                // Continue with next symbol
            }
        }
        
        // Sort by datetime and limit to 6 articles
        return allArticles.stream()
                .sorted((a, b) -> Long.compare(b.getDatetime(), a.getDatetime()))
                .limit(6)
                .collect(Collectors.toList());
    }
    
    private List<NewsArticleDto> getGeneralNews(WebClient webClient) {
        try {
            String url = String.format("/news?category=general&token=%s", apiKey);
            
            Mono<Object[]> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Object[].class);
            
            Object[] articles = response.block();
            
            if (articles != null) {
                return Arrays.stream(articles)
                        .map(article -> parseNewsArticle(article, false, null))
                        .filter(article -> article != null)
                        .limit(6)
                        .collect(Collectors.toList());
            }
            
            return new ArrayList<>();
            
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    private List<StockDto> getDefaultStocks() {
        return POPULAR_STOCK_SYMBOLS.subList(0, 10).stream()
                .map(symbol -> new StockDto(symbol, symbol, "US", "Stock"))
                .collect(Collectors.toList());
    }
    
    private String getDateRange(int days) {
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(days);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return fromDate.format(formatter) + "," + toDate.format(formatter);
    }
    
    private String extractNameFromProfile(Object profile) {
        // This would need to be implemented based on the actual Finnhub response structure
        // For now, return null to use symbol as name
        return null;
    }
    
    private List<StockDto> parseSearchResults(Object data) {
        List<StockDto> stocks = new ArrayList<>();
        
        try {
            // Convert Object to Map for easier parsing
            Map<String, Object> responseMap = (Map<String, Object>) data;
            
            // Extract the result array from Finnhub response
            Object resultObj = responseMap.get("result");
            
            if (resultObj instanceof List) {
                List<Object> resultList = (List<Object>) resultObj;
                
                for (Object item : resultList) {
                    if (item instanceof Map) {
                        Map<String, Object> itemMap = (Map<String, Object>) item;
                        
                        // Extract values from Finnhub search response
                        String symbol = getStringValue(itemMap, "symbol");
                        String description = getStringValue(itemMap, "description");
                        String displaySymbol = getStringValue(itemMap, "displaySymbol");
                        String type = getStringValue(itemMap, "type");
                        
                        // Use displaySymbol if available, otherwise use symbol
                        String finalSymbol = displaySymbol != null ? displaySymbol : symbol;
                        
                        // Use description as company name
                        String companyName = description != null ? description : finalSymbol;
                        
                        // Determine country based on symbol suffix
                        String country = "US"; // Default to US
                        if (finalSymbol != null) {
                            if (finalSymbol.endsWith(".TO")) country = "CA";
                            else if (finalSymbol.endsWith(".L")) country = "GB";
                            else if (finalSymbol.endsWith(".AS")) country = "NL";
                            else if (finalSymbol.endsWith(".MX")) country = "MX";
                            else if (finalSymbol.endsWith(".RO")) country = "RO";
                            else if (finalSymbol.endsWith(".SN")) country = "SN";
                            else if (finalSymbol.endsWith(".VI")) country = "VI";
                            else if (finalSymbol.endsWith(".WA")) country = "WA";
                        }
                        
                        if (finalSymbol != null && companyName != null) {
                            StockDto stock = new StockDto(finalSymbol, companyName, country, type != null ? type : "Stock");
                            stocks.add(stock);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            return new ArrayList<>();
        }
        
        return stocks;
    }
    
    private NewsArticleDto parseNewsArticle(Object article, boolean isCompanyNews, String symbol) {
        if (article == null) return null;
        
        try {
            // Convert Object to Map for easier parsing
            Map<String, Object> articleMap = (Map<String, Object>) article;
            
            // Extract values from Finnhub news response
            // Finnhub news response structure:
            // {"id": 123456, "headline": "...", "summary": "...", "source": "...", "url": "...", "datetime": 1234567890, "category": "...", "image": "..."}
            
            Long id = getLongValue(articleMap, "id");
            String headline = getStringValue(articleMap, "headline");
            String summary = getStringValue(articleMap, "summary");
            String source = getStringValue(articleMap, "source");
            String url = getStringValue(articleMap, "url");
            Long datetime = getLongValue(articleMap, "datetime");
            String category = getStringValue(articleMap, "category");
            String image = getStringValue(articleMap, "image");
            
            // Use current timestamp if id is not available
            if (id == null) {
                id = System.currentTimeMillis();
            }
            
            // Use current timestamp if datetime is not available
            if (datetime == null) {
                datetime = System.currentTimeMillis() / 1000;
            }
            
            // Ensure we have at least a headline
            if (headline == null || headline.trim().isEmpty()) {
                return null; // Skip articles without headlines
            }
            
            return new NewsArticleDto(
                id,
                headline,
                summary != null ? summary : "",
                source != null ? source : "Unknown Source",
                url != null ? url : "",
                datetime,
                category != null ? category : (isCompanyNews ? "company" : "general"),
                isCompanyNews ? (symbol != null ? symbol : "") : "",
                image
            );
            
        } catch (Exception e) {
            // If parsing fails, return null to skip this article
            return null;
        }
    }
    
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }
    
    private StockPriceDto parseStockQuote(String symbol, Object quote) {
        try {
            // Parse the Finnhub quote response
            // Finnhub returns: {"c": currentPrice, "d": change, "dp": changePercent, "h": high, "l": low, "o": open, "pc": previousClose, "t": timestamp}
            
            if (quote == null) {
                return getMockStockPrice(symbol);
            }
            
            // Convert Object to Map for easier parsing
            Map<String, Object> quoteMap = (Map<String, Object>) quote;
            
            // Extract values from Finnhub response
            Double currentPrice = getDoubleValue(quoteMap, "c");
            Double openPrice = getDoubleValue(quoteMap, "o");
            Double highPrice = getDoubleValue(quoteMap, "h");
            Double lowPrice = getDoubleValue(quoteMap, "l");
            Double previousClose = getDoubleValue(quoteMap, "pc");
            Double change = getDoubleValue(quoteMap, "d");
            Double changePercent = getDoubleValue(quoteMap, "dp");
            Long timestamp = getLongValue(quoteMap, "t");
            
            // If we don't have current price, fall back to mock data
            if (currentPrice == null) {
                return getMockStockPrice(symbol);
            }
            
            // Convert timestamp from Unix seconds to LocalDateTime
            LocalDateTime dateTime = timestamp != null ? 
                LocalDateTime.ofEpochSecond(timestamp, 0, java.time.ZoneOffset.UTC) : 
                LocalDateTime.now();
            
            return new StockPriceDto(
                symbol,
                getCompanyName(symbol),
                currentPrice,
                openPrice != null ? openPrice : currentPrice,
                highPrice != null ? highPrice : currentPrice,
                lowPrice != null ? lowPrice : currentPrice,
                previousClose != null ? previousClose : currentPrice,
                0L, // Volume not provided in quote endpoint
                dateTime,
                "NASDAQ",
                "USD"
            );
            
        } catch (Exception e) {
            // If parsing fails, return mock data
            return getMockStockPrice(symbol);
        }
    }
    
    private Double getDoubleValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }
    
    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return null;
    }
    
    private StockPriceDto getMockStockPrice(String symbol) {
        // Use realistic stock prices for better testing
        double currentPrice, openPrice, highPrice, lowPrice, previousClose;
        long volume;
        
        switch (symbol.toUpperCase()) {
            case "TSLA":
                currentPrice = 439.31;
                openPrice = 438.50;
                highPrice = 442.15;
                lowPrice = 435.80;
                previousClose = 437.20;
                volume = 45000000;
                break;
            case "AAPL":
                currentPrice = 175.50;
                openPrice = 174.80;
                highPrice = 176.20;
                lowPrice = 173.90;
                previousClose = 174.30;
                volume = 55000000;
                break;
            case "MSFT":
                currentPrice = 378.85;
                openPrice = 377.20;
                highPrice = 380.10;
                lowPrice = 376.50;
                previousClose = 377.80;
                volume = 25000000;
                break;
            case "GOOGL":
                currentPrice = 142.30;
                openPrice = 141.80;
                highPrice = 143.50;
                lowPrice = 141.20;
                previousClose = 141.90;
                volume = 18000000;
                break;
            case "NVDA":
                currentPrice = 875.20;
                openPrice = 870.50;
                highPrice = 880.15;
                lowPrice = 868.90;
                previousClose = 872.30;
                volume = 35000000;
                break;
            default:
                // Generate mock price data based on symbol for unknown stocks
                double basePrice = 100.0 + (symbol.hashCode() % 500);
                double variation = (Math.random() - 0.5) * 20; // ±10 variation
                currentPrice = basePrice + variation;
                
                openPrice = currentPrice + (Math.random() - 0.5) * 5;
                highPrice = Math.max(currentPrice, openPrice) + Math.random() * 3;
                lowPrice = Math.min(currentPrice, openPrice) - Math.random() * 3;
                previousClose = currentPrice + (Math.random() - 0.5) * 8;
                volume = (long) (1000000 + Math.random() * 5000000);
                break;
        }
        
        return new StockPriceDto(
            symbol,
            getCompanyName(symbol),
            Math.round(currentPrice * 100.0) / 100.0,
            Math.round(openPrice * 100.0) / 100.0,
            Math.round(highPrice * 100.0) / 100.0,
            Math.round(lowPrice * 100.0) / 100.0,
            Math.round(previousClose * 100.0) / 100.0,
            volume,
            LocalDateTime.now(),
            "NASDAQ",
            "USD"
        );
    }
    
    private String getCompanyName(String symbol) {
        // Map common symbols to company names
        switch (symbol.toUpperCase()) {
            case "AAPL": return "Apple Inc.";
            case "MSFT": return "Microsoft Corporation";
            case "GOOGL": return "Alphabet Inc.";
            case "AMZN": return "Amazon.com Inc.";
            case "TSLA": return "Tesla Inc.";
            case "META": return "Meta Platforms Inc.";
            case "NVDA": return "NVIDIA Corporation";
            case "NFLX": return "Netflix Inc.";
            case "ORCL": return "Oracle Corporation";
            case "CRM": return "Salesforce Inc.";
            case "ADBE": return "Adobe Inc.";
            case "INTC": return "Intel Corporation";
            case "AMD": return "Advanced Micro Devices Inc.";
            case "PYPL": return "PayPal Holdings Inc.";
            case "UBER": return "Uber Technologies Inc.";
            case "ZOOM": return "Zoom Video Communications Inc.";
            case "SPOT": return "Spotify Technology S.A.";
            case "SQ": return "Block Inc.";
            case "SHOP": return "Shopify Inc.";
            case "ROKU": return "Roku Inc.";
            default: return symbol + " Corporation";
        }
    }
}
