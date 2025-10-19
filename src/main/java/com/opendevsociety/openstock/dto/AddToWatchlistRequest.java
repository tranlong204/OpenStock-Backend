package com.opendevsociety.openstock.dto;

import jakarta.validation.constraints.NotBlank;

public class AddToWatchlistRequest {
    
    @NotBlank(message = "Symbol is required")
    private String symbol;
    
    @NotBlank(message = "Company is required")
    private String company;
    
    // Constructors
    public AddToWatchlistRequest() {}
    
    public AddToWatchlistRequest(String symbol, String company) {
        this.symbol = symbol;
        this.company = company;
    }
    
    // Getters and Setters
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
}
