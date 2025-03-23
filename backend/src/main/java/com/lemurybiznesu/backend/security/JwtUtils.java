package com.lemurybiznesu.backend.security;

import com.lemurybiznesu.backend.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;
    @Value("${app.cookie.domain}")
    private String cookieDomain;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(User user, UUID refreshTokenId) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("type", "ACCESS")
                .claim("version", user.getTokenVersion())
                .claim("refreshId", refreshTokenId.toString())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(accessTokenExpiration)))
                .signWith(key(), Jwts.SIG.HS512)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("type", "REFRESH")
                .claim("version", user.getTokenVersion())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(refreshTokenExpiration)))
                .signWith(key(), Jwts.SIG.HS512)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token);
    }

    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public UUID getUserId(Claims claims) {
        return UUID.fromString(claims.getSubject());
    }

    public Integer getTokenVersion(Claims claims) {
        return claims.get("version", Integer.class);
    }

    public UUID getRefreshTokenIdFromAccessToken(Claims claims) {
        return UUID.fromString(claims.get("refreshId", String.class));
    }

    public boolean isAccessToken(Claims claims) {
        return "ACCESS".equals(claims.get("type", String.class));
    }

    public boolean isRefreshToken(Claims claims) {
        return "REFRESH".equals(claims.get("type", String.class));
    }

    public void validateTokenVersion(Claims claims, User user) {
        int tokenVersion = claims.get("version", Integer.class);
        if (tokenVersion != user.getTokenVersion()) {
            throw new JwtException("Token version mismatch");
        }
    }

    public void setJwtCookies(HttpServletResponse response,
                              String accessToken,
                              String refreshToken) {
        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(true)
                .secure(true)
                .domain(cookieDomain)
                .path("/")
                .maxAge(accessTokenExpiration / 1000)
                .sameSite("Strict")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .domain(cookieDomain)
                .path("/api/auth/refresh")
                .maxAge(refreshTokenExpiration / 1000)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    public void clearJwtCookies(HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .domain(cookieDomain)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .domain(cookieDomain)
                .path("/api/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }
}
