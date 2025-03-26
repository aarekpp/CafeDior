package com.lemurybiznesu.backend.security;

import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.UserRepository;
import com.lemurybiznesu.backend.service.AuthService;
import com.lemurybiznesu.backend.service.RefreshTokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final RefreshTokenBlacklistService refreshTokenBlacklistService;
    private final AuthService authService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, RefreshTokenBlacklistService refreshTokenBlacklistService, AuthService authService, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.refreshTokenBlacklistService = refreshTokenBlacklistService;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CookieTokens cookieTokens = authService.getJwtTokensFromRequest(request);

        if(cookieTokens == null || isAuthenticationEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            if(isValidAccessToken(cookieTokens)){
                handleValidAccessToken(cookieTokens.getAccessToken());
                filterChain.doFilter(request, response);
                return;
            }

            if(isValidRefreshToken(cookieTokens)){
                handleRefreshToken(cookieTokens.getRefreshToken(), response);
                filterChain.doFilter(request, response);
                return;
            }

            handleInvalidTokens(response);
        }catch (Exception e){
            handleAuthenticationError(response, e);
        }
    }

    private boolean isAuthenticationEndpoint(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/auth/signin") || request.getRequestURI().startsWith("/api/auth/signup");
    }

    private boolean isValidAccessToken(CookieTokens tokens) {
        return tokens != null
                && StringUtils.hasText(tokens.getAccessToken())
                && jwtProvider.validateToken(tokens.getAccessToken(), false)
                && !refreshTokenBlacklistService.isRefreshTokenBlacklisted(tokens.getRefreshToken());
    }

    private boolean isValidRefreshToken(CookieTokens tokens) {
        return tokens != null
                && StringUtils.hasText(tokens.getRefreshToken())
                && jwtProvider.validateToken(tokens.getRefreshToken(), true)
                && !refreshTokenBlacklistService.isRefreshTokenBlacklisted(tokens.getRefreshToken());
    }

    private void handleValidAccessToken(String accessToken) {
        TokenDetails details = jwtProvider.decodeToken(accessToken, false);
        authenticateUser(details);
    }

    private void handleRefreshToken(String refreshToken, HttpServletResponse response) {
        TokenDetails details = jwtProvider.decodeToken(refreshToken, true);
        User user = userRepository.findById(UUID.fromString(details.getUserId()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getTokenVersion().equals(details.getTokenVersion())) {
            CookieTokens newTokens = generateNewTokens(user);
            authService.addTokensToResponse(response, newTokens);
            refreshTokenBlacklistService.blacklistRefreshToken(refreshToken, details);
            authenticateUser(jwtProvider.decodeToken(newTokens.getAccessToken(), false));
        } else {
            throw new RuntimeException("Token version invalid");
        }
    }

    private void handleInvalidTokens(HttpServletResponse response) throws IOException {
        authService.removeTokensFromResponse(response);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
    }

    private void handleAuthenticationError(HttpServletResponse response, Exception ex) throws IOException {
        authService.removeTokensFromResponse(response);
        response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Authentication error: " + ex.getMessage()
        );
    }

    private void authenticateUser(TokenDetails details) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                details.getUserId(),
                null,
                Collections.singleton(new SimpleGrantedAuthority(details.getRole()))
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private CookieTokens generateNewTokens(User user) {
        return new CookieTokens(
                jwtProvider.generateAccessToken(user),
                jwtProvider.generateRefreshToken(user)
        );
    }
}
