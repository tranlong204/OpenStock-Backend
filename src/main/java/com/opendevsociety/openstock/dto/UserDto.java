package com.opendevsociety.openstock.dto;

public class UserDto {
    
    private String id;
    private String email;
    private String name;
    private String country;
    private String investmentGoals;
    private String riskTolerance;
    private String preferredIndustry;
    
    // Constructors
    public UserDto() {}
    
    public UserDto(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getInvestmentGoals() {
        return investmentGoals;
    }
    
    public void setInvestmentGoals(String investmentGoals) {
        this.investmentGoals = investmentGoals;
    }
    
    public String getRiskTolerance() {
        return riskTolerance;
    }
    
    public void setRiskTolerance(String riskTolerance) {
        this.riskTolerance = riskTolerance;
    }
    
    public String getPreferredIndustry() {
        return preferredIndustry;
    }
    
    public void setPreferredIndustry(String preferredIndustry) {
        this.preferredIndustry = preferredIndustry;
    }
}
