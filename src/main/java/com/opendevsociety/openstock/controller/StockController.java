package com.opendevsociety.openstock.controller;

import com.opendevsociety.openstock.dto.StockDto;
import com.opendevsociety.openstock.dto.StockPriceDto;
import com.opendevsociety.openstock.service.FinnhubService;
import com.opendevsociety.openstock.service.WatchlistService;
import com.opendevsociety.openstock.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "*")
public class StockController {
    
    @Autowired
    private FinnhubService finnhubService;
    
    @Autowired
    private WatchlistService watchlistService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/search")
    public ResponseEntity<List<StockDto>> searchStocks(
            @RequestParam(required = false) String query,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        List<StockDto> stocks = finnhubService.searchStocks(query);
        
        // If user is authenticated, update watchlist status
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.getEmailFromToken(token);
                    // Get user ID from email (you might need to add this to AuthService)
                    // For now, we'll use email as userId
                    watchlistService.updateWatchlistStatus(stocks, email);
                }
            } catch (Exception e) {
                // Continue without updating watchlist status
            }
        }
        
        return ResponseEntity.ok(stocks);
    }
    
    @GetMapping("/price/{symbol}")
    public ResponseEntity<StockPriceDto> getStockPrice(@PathVariable String symbol) {
        StockPriceDto stockPrice = finnhubService.getStockPrice(symbol);
        return ResponseEntity.ok(stockPrice);
    }
}
