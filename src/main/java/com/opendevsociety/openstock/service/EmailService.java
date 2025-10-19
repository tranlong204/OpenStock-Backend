package com.opendevsociety.openstock.service;

import com.opendevsociety.openstock.entity.User;
import com.opendevsociety.openstock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private UserRepository userRepository;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Async
    public void sendWelcomeEmailAsync(String email, String name, String intro) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Welcome to Openstock - your open-source stock market toolkit!");
            
            String text = String.format(
                "Hi %s,\n\n" +
                "%s\n\n" +
                "Here's what you can do right now:\n" +
                "• Set up your watchlist to follow your favorite stocks\n" +
                "• Create price and volume alerts so you never miss a move\n" +
                "• Explore the dashboard for trends and the latest market news\n\n" +
                "We'll keep you informed with timely updates, insights, and alerts — so you can focus on making the right calls.\n\n" +
                "Best regards,\n" +
                "The Openstock Team",
                name, intro.replaceAll("<[^>]+>", "").replaceAll("&nbsp;", " ")
            );
            
            message.setText(text);
            mailSender.send(message);
            
        } catch (Exception e) {
            // Log error but don't throw exception for async method
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }
    
    @Async
    public void sendNewsSummaryEmailAsync(String email, String newsContent) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("📈 Market News Summary Today - " + getFormattedTodayDate());
            
            String text = String.format(
                "Hi there,\n\n" +
                "Here's today's market news summary:\n\n" +
                "%s\n\n" +
                "Stay informed and make smart investment decisions!\n\n" +
                "Best regards,\n" +
                "The Openstock Team",
                newsContent.replaceAll("<[^>]+>", "").replaceAll("&nbsp;", " ")
            );
            
            message.setText(text);
            mailSender.send(message);
            
        } catch (Exception e) {
            System.err.println("Failed to send news summary email: " + e.getMessage());
        }
    }
    
    public List<User> getAllUsersForNewsEmail() {
        return userRepository.findAll().stream()
                .filter(user -> user.getEmail() != null && user.getName() != null && user.isActive())
                .collect(java.util.stream.Collectors.toList());
    }
    
    private String getFormattedTodayDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        return today.format(formatter);
    }
}
