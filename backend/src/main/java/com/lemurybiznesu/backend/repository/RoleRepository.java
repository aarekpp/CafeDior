package com.lemurybiznesu.backend.repository;

import com.lemurybiznesu.backend.model.entity.ERole;
import com.lemurybiznesu.backend.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole name);
}
