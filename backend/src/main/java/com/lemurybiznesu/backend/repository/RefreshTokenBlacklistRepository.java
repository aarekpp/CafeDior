package com.lemurybiznesu.backend.repository;

import com.lemurybiznesu.backend.model.entity.RefreshTokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenBlacklistRepository extends JpaRepository<RefreshTokenBlacklist, Long> {
    boolean existsByToken(String token);
}
