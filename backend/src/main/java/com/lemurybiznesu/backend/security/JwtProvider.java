package com.lemurybiznesu.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lemurybiznesu.backend.model.entity.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.access.secret}")
    private String accessSecret;
    @Value("${jwt.refresh.secret}")
    private String refreshSecret;
    @Getter @Value("${jwt.access.expiration}")
    private int accessExpiration;
    @Getter @Value("${jwt.refresh.expiration}")
    private int refreshExpiration;

    public String generateAccessToken(User user) {
        return buildToken(user, accessSecret, accessExpiration);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, refreshSecret, refreshExpiration);
    }

    public boolean validateToken(String token, boolean isRefresh) {
        if(token == null || token.isEmpty()) {
            return false;
        }

        try{
            String secret = isRefresh ? refreshSecret : accessSecret;
            Algorithm algorithm = Algorithm.HMAC512(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        }catch (JWTVerificationException ex){
            throw new RuntimeException(ex);
        }
    }

    public TokenDetails decodeToken(String token, boolean isRefresh) {
        try{
            String secret = isRefresh ? refreshSecret : accessSecret;
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(secret))
                    .build().verify(token);
            return new TokenDetails(jwt.getSubject(), jwt.getExpiresAt(), jwt.getClaim("role").asString(), jwt.getClaim("token_version").asInt());
        }catch (JWTVerificationException ex){
            throw new RuntimeException("invalid token");
        }
    }

    private String buildToken(User user, String secret, int expiration) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration * 1000L);

        return JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("role", user.getRole().getName().toString())
                .withClaim("token_version", user.getTokenVersion())
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC512(secret));
    }
}
