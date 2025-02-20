package com.project.superleague.service;

import com.project.superleague.model.UserPrincipal;
import com.project.superleague.model.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTests {
    @InjectMocks
    private JWTServiceImpl jwtService;

    private Users user;
    private UserDetails userDetails;

    @BeforeEach
    public void init() {
        user = Users.builder()
                .username("admin")
                .password("a1#a1$a1####")
                .build();

        user.setId(1L);

        userDetails = new UserPrincipal(user);
    }

    @Test
    public void JWTService_GenerateToken_ReturnsStringJWTToken() {
        String username = "admin";

        String token = jwtService.generateToken(username);

        Assertions.assertThat(token).isNotNull();
    }

    @Test
    public void JWTService_ExtractUsername_ReturnsStringUsername() {
        String username = "admin";

        String token = jwtService.generateToken(username);

        String extractedUsername = jwtService.extractUsername(token);

        Assertions.assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    public void JWTService_ValidateToken_ReturnsBoolean() {
        String username = "admin";

        String token = jwtService.generateToken(username);

        boolean isValid = jwtService.validateToken(token, userDetails);

        Assertions.assertThat(isValid).isTrue();
    }
}