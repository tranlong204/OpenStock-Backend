package com.opendevsociety.openstock.controller;

import com.opendevsociety.openstock.dto.AddToWatchlistRequest;
import com.opendevsociety.openstock.dto.WatchlistItemDto;
import com.opendevsociety.openstock.service.AuthService;
import com.opendevsociety.openstock.service.WatchlistService;
import com.opendevsociety.openstock.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/watchlist")
@CrossOrigin(origins = "*")
public class WatchlistController {
    
    @Autowired
    private WatchlistService watchlistService;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping
    public ResponseEntity<List<WatchlistItemDto>> getUserWatchlist(
            @RequestHeader("Authorization") String authHeader) {
        
        String userId = getUserIdFromToken(authHeader);
        List<WatchlistItemDto> watchlist = watchlistService.getUserWatchlist(userId);
        return ResponseEntity.ok(watchlist);
    }
    
    @PostMapping
    public ResponseEntity<WatchlistItemDto> addToWatchlist(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AddToWatchlistRequest request) {
        
        String userId = getUserIdFromToken(authHeader);
        WatchlistItemDto item = watchlistService.addToWatchlist(userId, request.getSymbol(), request.getCompany());
        return ResponseEntity.ok(item);
    }
    
    @DeleteMapping("/{symbol}")
    public ResponseEntity<Map<String, String>> removeFromWatchlist(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String symbol) {
        
        String userId = getUserIdFromToken(authHeader);
        watchlistService.removeFromWatchlist(userId, symbol);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Removed from watchlist");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check/{symbol}")
    public ResponseEntity<Boolean> isInWatchlist(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String symbol) {
        
        String userId = getUserIdFromToken(authHeader);
        boolean isInWatchlist = watchlistService.isInWatchlist(userId, symbol);
        return ResponseEntity.ok(isInWatchlist);
    }
    
    private String getUserIdFromToken(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);
        return authService.getUserByEmail(email)
                .map(user -> user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
