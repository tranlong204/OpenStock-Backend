package com.opendevsociety.openstock.controller;

import com.opendevsociety.openstock.dto.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/openstock-test",
    "app.jwt.secret=test-secret-key-for-testing-only",
    "app.finnhub.api-key=test-key",
    "java.version=21"
})
class AuthControllerTest {

    @Test
    void contextLoads() {
        // Basic test to ensure Spring context loads
        assertTrue(true);
    }

    @Test
    void testSignUpRequest() {
        SignUpRequest request = new SignUpRequest();
        request.setFullName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setCountry("United States");
        request.setInvestmentGoals("Growth");
        request.setRiskTolerance("Medium");
        request.setPreferredIndustry("Technology");

        assertEquals("Test User", request.getFullName());
        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        assertEquals("United States", request.getCountry());
        assertEquals("Growth", request.getInvestmentGoals());
        assertEquals("Medium", request.getRiskTolerance());
        assertEquals("Technology", request.getPreferredIndustry());
    }
}
