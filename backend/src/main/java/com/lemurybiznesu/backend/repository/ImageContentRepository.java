package com.lemurybiznesu.backend.repository;

import com.lemurybiznesu.backend.model.entity.EImageContent;
import com.lemurybiznesu.backend.model.entity.ImageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageContentRepository extends JpaRepository<ImageContent, Integer> {
    boolean existsByImageContentType(EImageContent imageContentType);
    Optional<ImageContent> findById(Long id);
    Optional<ImageContent> findByImageContentType(EImageContent imageContentType);
}
