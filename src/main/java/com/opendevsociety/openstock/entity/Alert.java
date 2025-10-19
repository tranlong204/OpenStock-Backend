package com.opendevsociety.openstock.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "alerts")
public class Alert {
    
    @Id
    private String id;
    
    @Field("user_id")
    private String userId;
    
    private String symbol;
    private String company;
    private String alertName;
    
    @Field("alert_type")
    private AlertType alertType;
    
    private double threshold;
    
    @Field("current_price")
    private double currentPrice;
    
    @Field("change_percent")
    private Double changePercent;
    
    private boolean active;
    
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    public enum AlertType {
        UPPER, LOWER
    }
    
    // Constructors
    public Alert() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }
    
    public Alert(String userId, String symbol, String company, String alertName, 
                 AlertType alertType, double threshold) {
        this();
        this.userId = userId;
        this.symbol = symbol;
        this.company = company;
        this.alertName = alertName;
        this.alertType = alertType;
        this.threshold = threshold;
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
    
    public String getAlertName() {
        return alertName;
    }
    
    public void setAlertName(String alertName) {
        this.alertName = alertName;
    }
    
    public AlertType getAlertType() {
        return alertType;
    }
    
    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }
    
    public double getThreshold() {
        return threshold;
    }
    
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }
    
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public Double getChangePercent() {
        return changePercent;
    }
    
    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
