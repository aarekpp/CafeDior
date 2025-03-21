package com.lemurybiznesu.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles",
        indexes = @Index(name = "idx_role_name", columnList = "name"),
        uniqueConstraints = @UniqueConstraint(name = "uc_role_name", columnNames = "name"))
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "name")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, columnDefinition = "BIGINT UNSIGNED AUTO_INCREMENT")
    private Long id;

    @NotNull(message = "Role name cannot be null")
    @Size(min = 20, max = 20)
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(name = "name", nullable = false, unique = true, updatable = false, length = 20,
            columnDefinition = "VARCHAR(20) CHECK (name IN ('ROLE_USER','ROLE_MODERATOR','ROLE_ADMIN'))")
    private ERole name;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
