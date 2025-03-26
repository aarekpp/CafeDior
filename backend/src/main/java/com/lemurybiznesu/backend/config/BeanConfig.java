package com.lemurybiznesu.backend.config;

import com.lemurybiznesu.backend.repository.UserRepository;
import com.lemurybiznesu.backend.security.JwtAuthenticationFilter;
import com.lemurybiznesu.backend.security.JwtProvider;
import com.lemurybiznesu.backend.service.AuthService;
import com.lemurybiznesu.backend.service.RefreshTokenBlacklistService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtProvider jwtProvider,
            RefreshTokenBlacklistService refreshTokenBlacklistService,
            AuthService authService,
            UserRepository userRepository
    ) {
        return new JwtAuthenticationFilter(
                jwtProvider,
                refreshTokenBlacklistService,
                authService,
                userRepository
        );
    }
}
