package com.lemurybiznesu.backend.repository;

import com.lemurybiznesu.backend.model.entity.AboutContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AboutContentRepository extends JpaRepository<AboutContent, Long> {
    Optional<AboutContent> findFirstByOrderByCreatedAtAsc();
}
