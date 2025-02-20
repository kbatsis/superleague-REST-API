package com.project.superleague.service;

import com.project.superleague.model.Users;
import com.project.superleague.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Users user;

    @BeforeEach
    public void init() {
        user = Users.builder()
                .username("admin")
                .password("a1#a1$a1####")
                .build();

        user.setId(1L);
    }

    @Test
    public void UserDetailsService_LoadUserByUsername_ReturnsUserPrincipal() {
        String username = "admin";

        when(userRepository.findByUsername(username)).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Assertions.assertThat(userDetails.getUsername()).isEqualTo("admin");
    }

    @Test
    public void UserDetailsService_LoadUserByUsername_ThrowsUserNotFoundException() {
        String username = "nikosm";

        when(userRepository.findByUsername(username)).thenReturn(null);

        Assertions.assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username)).isInstanceOf(UsernameNotFoundException.class);
    }
}