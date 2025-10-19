package com.opendevsociety.openstock.dto;

public class AuthResponse {
    
    private boolean success;
    private String token;
    private String message;
    private UserDto user;
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(boolean success, String token, String message, UserDto user) {
        this.success = success;
        this.token = token;
        this.message = message;
        this.user = user;
    }
    
    public static AuthResponse success(String token, UserDto user) {
        return new AuthResponse(true, token, "Authentication successful", user);
    }
    
    public static AuthResponse failure(String message) {
        return new AuthResponse(false, null, message, null);
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public UserDto getUser() {
        return user;
    }
    
    public void setUser(UserDto user) {
        this.user = user;
    }
}
