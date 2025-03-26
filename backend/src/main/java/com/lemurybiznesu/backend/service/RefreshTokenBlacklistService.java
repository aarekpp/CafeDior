package com.lemurybiznesu.backend.service;

import com.lemurybiznesu.backend.model.entity.RefreshTokenBlacklist;
import com.lemurybiznesu.backend.repository.RefreshTokenBlacklistRepository;
import com.lemurybiznesu.backend.security.TokenDetails;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenBlacklistService {
    private final RefreshTokenBlacklistRepository refreshTokenBlacklistRepository;

    public RefreshTokenBlacklistService(RefreshTokenBlacklistRepository refreshTokenBlacklistRepository) {
        this.refreshTokenBlacklistRepository = refreshTokenBlacklistRepository;
    }

    @Transactional
    public void blacklistRefreshToken(String token, TokenDetails refreshTokenDetails) {
        try{
            RefreshTokenBlacklist refreshTokenBlacklist = new RefreshTokenBlacklist();
            refreshTokenBlacklist.setToken(token);
            refreshTokenBlacklist.setExpiryTime(refreshTokenDetails.getExpirationDate());
            refreshTokenBlacklistRepository.save(refreshTokenBlacklist);
        }catch (Exception e){
            throw new RuntimeException("Error while blacklisting refresh token");
        }
    }

    public boolean isRefreshTokenBlacklisted(String token) {
        try{
            if(token == null || token.isEmpty()) {
                return false;
            }
            return refreshTokenBlacklistRepository.existsByToken(token);
        } catch (Exception e) {
            throw new RuntimeException("Error while blacklisting refresh token");
        }
    }
}
