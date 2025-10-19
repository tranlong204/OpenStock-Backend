package com.opendevsociety.openstock.dto;

import java.time.LocalDateTime;

public class WatchlistItemDto {
    
    private String id;
    private String userId;
    private String symbol;
    private String company;
    private LocalDateTime addedAt;
    
    // Constructors
    public WatchlistItemDto() {}
    
    public WatchlistItemDto(String userId, String symbol, String company) {
        this.userId = userId;
        this.symbol = symbol;
        this.company = company;
        this.addedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getCompany() {
        return company;
    }
    
    public void setCompany(String company) {
        this.company = company;
    }
    
    public LocalDateTime getAddedAt() {
        return addedAt;
    }
    
    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
