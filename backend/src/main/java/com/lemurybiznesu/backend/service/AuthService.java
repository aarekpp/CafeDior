package com.lemurybiznesu.backend.service;

import com.lemurybiznesu.backend.model.dto.request.SigninRequest;
import com.lemurybiznesu.backend.model.dto.response.AuthResponse;
import com.lemurybiznesu.backend.model.entity.User;
import com.lemurybiznesu.backend.repository.UserRepository;
import com.lemurybiznesu.backend.security.CookieTokens;
import com.lemurybiznesu.backend.security.JwtProvider;
import com.lemurybiznesu.backend.security.TokenDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenBlacklistService refreshTokenBlacklistService;
    private final UserService userService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, RefreshTokenBlacklistService refreshTokenBlacklistService, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.refreshTokenBlacklistService = refreshTokenBlacklistService;
        this.userService = userService;
    }

    public AuthResponse authenticateUser(HttpServletRequest request, HttpServletResponse response, SigninRequest signinRequest) {
        try{
            User user = userRepository.findByEmail(signinRequest.getEmail())
                    .filter(u -> passwordEncoder.matches(signinRequest.getPassword(), u.getPassword()))
                    .orElseThrow(() -> new RuntimeException("Invalid email or password"));

            invalidateOldTokens(request);
            CookieTokens cookieTokens = new CookieTokens(jwtProvider.generateAccessToken(user), jwtProvider.generateRefreshToken(user));
            addTokensToResponse(response, cookieTokens);

            return new AuthResponse(user.getId().toString(), user.getRole().getName().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthResponse verifyToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        String userId = authentication.getName();
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse(
                userId,
                user.getRole().getName().toString()
        );
    }

    public CookieTokens getJwtTokensFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        CookieTokens cookieTokens = new CookieTokens();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("ACCESS_TOKEN")) {
                    cookieTokens.setAccessToken(cookie.getValue());
                }
                if(cookie.getName().equals("REFRESH_TOKEN")) {
                    cookieTokens.setRefreshToken(cookie.getValue());
                }
            }
            return cookieTokens;
        }
        return null;
    }

    public void addTokensToResponse(HttpServletResponse response, CookieTokens cookieTokens) {
        addCookie(response, "ACCESS_TOKEN", cookieTokens.getAccessToken(), jwtProvider.getAccessExpiration());
        addCookie(response, "REFRESH_TOKEN", cookieTokens.getRefreshToken(), jwtProvider.getRefreshExpiration());
    }

    public void removeTokensFromResponse(HttpServletResponse response) {
        removeCookie(response, "ACCESS_TOKEN");
        removeCookie(response, "REFRESH_TOKEN");
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try{
            CookieTokens cookieTokens = getJwtTokensFromRequest(request);
            String accessToken = cookieTokens.getAccessToken();
            String refreshToken = cookieTokens.getRefreshToken();

            if(jwtProvider.validateToken(accessToken, false) && jwtProvider.validateToken(refreshToken, true)) {
                TokenDetails refreshTokenDetails = jwtProvider.decodeToken(refreshToken, true);
                userService.incrementTokenVersion(UUID.fromString(refreshTokenDetails.getUserId()));
                refreshTokenBlacklistService.blacklistRefreshToken(refreshToken, refreshTokenDetails);
                removeTokensFromResponse(response);
            }
        }catch (Exception ex) {
            throw new RuntimeException("Unable to log out", ex);
        }
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        String cookie = String.format("%s=%s; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=%d", name, value, maxAge);
        response.addHeader("Set-Cookie", cookie);
    }

    private void removeCookie(HttpServletResponse response, String name) {
        String cookie = String.format("%s=; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=0", name);
        response.addHeader("Set-Cookie", cookie);
    }

    private void invalidateOldTokens(HttpServletRequest request) {
        CookieTokens cookieTokens = getJwtTokensFromRequest(request);
        if(cookieTokens != null) {
            if(StringUtils.hasText(cookieTokens.getRefreshToken())) {
                TokenDetails refreshTokenDetails = jwtProvider.decodeToken(cookieTokens.getRefreshToken(), true);
                refreshTokenBlacklistService.blacklistRefreshToken(cookieTokens.getRefreshToken(), refreshTokenDetails);
            }
        }
    }
}
