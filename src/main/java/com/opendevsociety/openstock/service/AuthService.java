package com.opendevsociety.openstock.service;

import com.opendevsociety.openstock.dto.AuthResponse;
import com.opendevsociety.openstock.dto.SignInRequest;
import com.opendevsociety.openstock.dto.SignUpRequest;
import com.opendevsociety.openstock.dto.UserDto;
import com.opendevsociety.openstock.entity.User;
import com.opendevsociety.openstock.repository.UserRepository;
import com.opendevsociety.openstock.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private EmailService emailService;
    
    public AuthResponse signUp(SignUpRequest request) {
        try {
            // Check if user already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                return AuthResponse.failure("User with this email already exists");
            }
            
            // Create new user
            User user = new User();
            user.setEmail(request.getEmail());
            user.setName(request.getFullName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setCountry(request.getCountry());
            user.setInvestmentGoals(request.getInvestmentGoals());
            user.setRiskTolerance(request.getRiskTolerance());
            user.setPreferredIndustry(request.getPreferredIndustry());
            
            User savedUser = userRepository.save(user);
            
            // Generate JWT token
            String token = jwtUtil.generateToken(savedUser.getEmail());
            
            // Convert to DTO
            UserDto userDto = convertToDto(savedUser);
            
            // Send welcome email asynchronously
            emailService.sendWelcomeEmailAsync(savedUser.getEmail(), savedUser.getName(), 
                generateWelcomeIntro(savedUser));
            
            return AuthResponse.success(token, userDto);
            
        } catch (Exception e) {
            return AuthResponse.failure("Sign up failed: " + e.getMessage());
        }
    }
    
    public AuthResponse signIn(SignInRequest request) {
        try {
            Optional<User> userOpt = userRepository.findByEmailAndActiveTrue(request.getEmail());
            
            if (userOpt.isEmpty()) {
                return AuthResponse.failure("Invalid email or password");
            }
            
            User user = userOpt.get();
            
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return AuthResponse.failure("Invalid email or password");
            }
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());
            
            // Convert to DTO
            UserDto userDto = convertToDto(user);
            
            return AuthResponse.success(token, userDto);
            
        } catch (Exception e) {
            return AuthResponse.failure("Sign in failed: " + e.getMessage());
        }
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto(user.getId(), user.getEmail(), user.getName());
        dto.setCountry(user.getCountry());
        dto.setInvestmentGoals(user.getInvestmentGoals());
        dto.setRiskTolerance(user.getRiskTolerance());
        dto.setPreferredIndustry(user.getPreferredIndustry());
        return dto;
    }
    
    private String generateWelcomeIntro(User user) {
        StringBuilder intro = new StringBuilder();
        intro.append("<p class=\"mobile-text\" style=\"margin:0 0 20px 0;font-size:16px;line-height:1.6;color:#4b5563;\">");
        intro.append("Welcome to Openstock! We're excited to have you join our community of smart investors. ");
        intro.append("Based on your profile, we'll help you track markets and make informed decisions. ");
        intro.append("Your investment goals: ").append(user.getInvestmentGoals()).append(", ");
        intro.append("Risk tolerance: ").append(user.getRiskTolerance()).append(", ");
        intro.append("Preferred industry: ").append(user.getPreferredIndustry()).append(". ");
        intro.append("Let's get started on your investment journey!");
        intro.append("</p>");
        return intro.toString();
    }
}
