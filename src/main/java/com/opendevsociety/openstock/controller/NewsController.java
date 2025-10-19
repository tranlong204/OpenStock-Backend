package com.opendevsociety.openstock.controller;

import com.opendevsociety.openstock.dto.NewsArticleDto;
import com.opendevsociety.openstock.service.FinnhubService;
import com.opendevsociety.openstock.service.WatchlistService;
import com.opendevsociety.openstock.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsController {
    
    @Autowired
    private FinnhubService finnhubService;
    
    @Autowired
    private WatchlistService watchlistService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping
    public ResponseEntity<List<NewsArticleDto>> getNews(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        List<String> symbols = null;
        
        // If user is authenticated, get their watchlist symbols
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    String userId = jwtUtil.getEmailFromToken(token);
                    symbols = watchlistService.getUserWatchlistSymbols(userId);
                }
            } catch (Exception e) {
                // Continue without user-specific symbols
            }
        }
        
        List<NewsArticleDto> news = finnhubService.getNews(symbols);
        return ResponseEntity.ok(news);
    }
    
    @GetMapping("/symbols")
    public ResponseEntity<List<NewsArticleDto>> getNewsBySymbols(
            @RequestParam List<String> symbols) {
        
        List<NewsArticleDto> news = finnhubService.getNews(symbols);
        return ResponseEntity.ok(news);
    }
}
