package com.opendevsociety.openstock.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpRequest {
    
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    @NotBlank(message = "Investment goals is required")
    private String investmentGoals;
    
    @NotBlank(message = "Risk tolerance is required")
    private String riskTolerance;
    
    @NotBlank(message = "Preferred industry is required")
    private String preferredIndustry;
    
    // Constructors
    public SignUpRequest() {}
    
    public SignUpRequest(String fullName, String email, String password, String country,
                        String investmentGoals, String riskTolerance, String preferredIndustry) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.country = country;
        this.investmentGoals = investmentGoals;
        this.riskTolerance = riskTolerance;
        this.preferredIndustry = preferredIndustry;
    }
    
    // Getters and Setters
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
