package com.lemurybiznesu.backend.config;

import com.lemurybiznesu.backend.model.entity.ERole;
import com.lemurybiznesu.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("#{'${app.allowed-origins}'.split(',')}")
    private List<String> allowedOrigins;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src 'self' data:"))
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .preload(true)
                                .maxAgeInSeconds(63072000)
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/swagger-ui.html",
                                "/api/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/auth/signin",
                                "/api/auth/signup",
                                "/api/auth/verify-token"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/logout").authenticated()
                        .requestMatchers("/api/auth/**").denyAll()
                        .requestMatchers("/api/user/**").authenticated()
                        .requestMatchers("/api/user/**").denyAll()
                        .requestMatchers(HttpMethod.GET, "/api/about").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/about").hasAnyAuthority(String.valueOf(ERole.MODERATOR))
                        .requestMatchers(HttpMethod.PUT, "/api/about").hasAnyAuthority(String.valueOf(ERole.MODERATOR))
                        .requestMatchers(HttpMethod.DELETE, "/api/about").hasAnyAuthority(String.valueOf(ERole.MODERATOR))
                        .requestMatchers("/api/about/**").denyAll()
                        .requestMatchers(HttpMethod.POST, "/api/images/**").hasAnyAuthority(String.valueOf(ERole.MODERATOR))
                        .requestMatchers(HttpMethod.PUT, "/api/images/**").hasAnyAuthority(String.valueOf(ERole.MODERATOR))
                        .requestMatchers(HttpMethod.DELETE, "/api/images/**").hasAnyAuthority(String.valueOf(ERole.MODERATOR))
                        .requestMatchers("/api/images/**").denyAll()
                        .requestMatchers(HttpMethod.GET,"/api/home/**").permitAll()
                        .requestMatchers("/api/home/**").denyAll()
                        .requestMatchers(HttpMethod.GET, "/api/images/**").permitAll()
                        .requestMatchers("/api/images/**").denyAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
