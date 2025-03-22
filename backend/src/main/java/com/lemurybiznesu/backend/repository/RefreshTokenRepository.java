package com.lemurybiznesu.backend.repository;

import com.lemurybiznesu.backend.model.entity.RefreshToken;
import com.lemurybiznesu.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserAndRevokedFalseAndExpiryDateAfter(User user, LocalDateTime expiryDate);
    void deleteAllByUser(User user);
}
