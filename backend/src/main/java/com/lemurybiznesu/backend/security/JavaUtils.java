package com.lemurybiznesu.backend.security;

import com.lemurybiznesu.backend.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JavaUtils {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;

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
}
