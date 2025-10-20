package com.opendevsociety.openstock.controller;

import com.opendevsociety.openstock.dto.UserDto;
import com.opendevsociety.openstock.service.AuthService;
import com.opendevsociety.openstock.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.getEmailFromToken(token);
            
            return authService.getUserByEmail(email)
                .map(user -> {
                    UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getName());
                    userDto.setCountry(user.getCountry());
                    userDto.setInvestmentGoals(user.getInvestmentGoals());
                    userDto.setRiskTolerance(user.getRiskTolerance());
                    userDto.setPreferredIndustry(user.getPreferredIndustry());
                    return ResponseEntity.ok(userDto);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
