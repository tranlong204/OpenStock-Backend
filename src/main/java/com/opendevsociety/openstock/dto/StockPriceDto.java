package com.opendevsociety.openstock.dto;

import java.time.LocalDateTime;

public class StockPriceDto {
    
    private String symbol;
    private String name;
    private Double currentPrice;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double previousClose;
    private Long volume;
    private LocalDateTime timestamp;
    private String exchange;
    private String currency;
    
    // Constructors
    public StockPriceDto() {}
    
    public StockPriceDto(String symbol, String name, Double currentPrice) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
    }
    
    public StockPriceDto(String symbol, String name, Double currentPrice, Double openPrice, 
                        Double highPrice, Double lowPrice, Double previousClose, 
                        Long volume, LocalDateTime timestamp, String exchange, String currency) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.previousClose = previousClose;
        this.volume = volume;
        this.timestamp = timestamp;
        this.exchange = exchange;
        this.currency = currency;
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
    
    public Double getCurrentPrice() {
        return currentPrice;
    }
    
    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public Double getOpenPrice() {
        return openPrice;
    }
    
    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }
    
    public Double getHighPrice() {
        return highPrice;
    }
    
    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }
    
    public Double getLowPrice() {
        return lowPrice;
    }
    
    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }
    
    public Double getPreviousClose() {
        return previousClose;
    }
    
    public void setPreviousClose(Double previousClose) {
        this.previousClose = previousClose;
    }
    
    public Long getVolume() {
        return volume;
    }
    
    public void setVolume(Long volume) {
        this.volume = volume;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getExchange() {
        return exchange;
    }
    
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    // Helper methods
    public Double getPriceChange() {
        if (currentPrice != null && previousClose != null) {
            return currentPrice - previousClose;
        }
        return null;
    }
    
    public Double getPriceChangePercent() {
        if (currentPrice != null && previousClose != null && previousClose != 0) {
            return ((currentPrice - previousClose) / previousClose) * 100;
        }
        return null;
    }
}
