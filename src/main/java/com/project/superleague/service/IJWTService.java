package com.project.superleague.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJWTService {
    String generateToken(String username);
    String extractUsername(String token);
    boolean validateToken(String token, UserDetails userDetails);
}