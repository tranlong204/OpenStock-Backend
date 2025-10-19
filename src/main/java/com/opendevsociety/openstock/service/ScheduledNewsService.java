package com.opendevsociety.openstock.service;

import com.opendevsociety.openstock.dto.NewsArticleDto;
import com.opendevsociety.openstock.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ScheduledNewsService {
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private FinnhubService finnhubService;
    
    @Autowired
    private WatchlistService watchlistService;
    
    // Run daily at 12:00 PM
    @Scheduled(cron = "0 0 12 * * ?")
    public void sendDailyNewsSummary() {
        try {
            List<User> users = emailService.getAllUsersForNewsEmail();
            
            if (users.isEmpty()) {
                System.out.println("No users found for news email");
                return;
            }
            
            for (User user : users) {
                try {
                    // Get user's watchlist symbols
                    List<String> symbols = watchlistService.getUserWatchlistSymbols(user.getId());
                    
                    // Fetch news for user's watchlist
                    List<NewsArticleDto> articles = finnhubService.getNews(symbols);
                    
                    // If no articles from watchlist, get general news
                    if (articles.isEmpty()) {
                        articles = finnhubService.getNews(null);
                    }
                    
                    // Limit to 6 articles
                    articles = articles.stream().limit(6).collect(java.util.stream.Collectors.toList());
                    
                    // Generate news content
                    String newsContent = generateNewsContent(articles);
                    
                    // Send email asynchronously
                    emailService.sendNewsSummaryEmailAsync(user.getEmail(), newsContent);
                    
                } catch (Exception e) {
                    System.err.println("Failed to process news for user: " + user.getEmail() + " - " + e.getMessage());
                }
            }
            
            System.out.println("Daily news summary emails sent successfully");
            
        } catch (Exception e) {
            System.err.println("Failed to send daily news summary: " + e.getMessage());
        }
    }
    
    private String generateNewsContent(List<NewsArticleDto> articles) {
        if (articles.isEmpty()) {
            return "No market news available today. Please check back tomorrow.";
        }
        
        StringBuilder content = new StringBuilder();
        content.append("Here's today's market news summary:\n\n");
        
        for (NewsArticleDto article : articles) {
            content.append("📰 ").append(article.getHeadline()).append("\n");
            content.append(article.getSummary()).append("\n");
            content.append("Source: ").append(article.getSource()).append("\n");
            content.append("Read more: ").append(article.getUrl()).append("\n\n");
        }
        
        return content.toString();
    }
}
