package com.lemurybiznesu.backend.repository;

import com.lemurybiznesu.backend.model.entity.ContactContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactContentRepository extends JpaRepository<ContactContent, Long> {
    List<ContactContent> findAllByOrderByDisplayOrderAsc();
}
