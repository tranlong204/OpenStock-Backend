package com.opendevsociety.openstock.dto;

public class StockDto {
    
    private String symbol;
    private String name;
    private String exchange;
    private String type;
    private boolean isInWatchlist;
    
    // Constructors
    public StockDto() {}
    
    public StockDto(String symbol, String name, String exchange, String type) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        this.type = type;
        this.isInWatchlist = false;
    }
    
    public StockDto(String symbol, String name, String exchange, String type, boolean isInWatchlist) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        this.type = type;
        this.isInWatchlist = isInWatchlist;
    }
    
    // Getters and Setters
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getExchange() {
        return exchange;
    }
    
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public boolean isInWatchlist() {
        return isInWatchlist;
    }
    
    public void setInWatchlist(boolean inWatchlist) {
        isInWatchlist = inWatchlist;
    }
}
