package com.lemurybiznesu.backend.repository;

import com.lemurybiznesu.backend.model.entity.MenuContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuContentRepository extends JpaRepository<MenuContent, Long> {
    List<MenuContent> findAllByOrderByDisplayOrderAsc();
}
