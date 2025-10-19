package com.opendevsociety.openstock.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.time.LocalDateTime;

@Document(collection = "watchlist")
@CompoundIndexes({
    @CompoundIndex(name = "user_symbol", def = "{'userId': 1, 'symbol': 1}", unique = true)
})
public class WatchlistItem {
    
    @Id
    private String id;
    
    @Field("user_id")
    private String userId;
    
    private String symbol;
    private String company;
    
    @Field("added_at")
    private LocalDateTime addedAt;
    
    // Constructors
    public WatchlistItem() {
        this.addedAt = LocalDateTime.now();
    }
    
    public WatchlistItem(String userId, String symbol, String company) {
        this();
        this.userId = userId;
        this.symbol = symbol;
        this.company = company;
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
