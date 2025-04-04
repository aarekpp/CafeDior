package com.lemurybiznesu.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "specialities_content", indexes = {@Index(name = "idx_specialities_order", columnList = "display_order")})
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpecialityContent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    @EqualsAndHashCode.Include
    private String id;

    @NotBlank(message = "Main title cannot be blank")
    @Size(max = 100)
    @Column(name = "main_title", nullable = false, length = 100, columnDefinition = "VARCHAR(100) COLLATE utf8mb4_unicode_ci")
    private String mainTitle;

    @NotBlank(message = "Second title cannot be blank")
    @Size(max = 200)
    @Column(name = "second_title", length = 200, columnDefinition = "VARCHAR(200) COLLATE utf8mb4_unicode_ci")
    private String secondTitle;

    @NotBlank(message = "Description cannot be blank")
    @Column(nullable = false, columnDefinition = "TEXT COLLATE utf8mb4_unicode_ci")
    private String description;

    @NotNull(message = "Display order cannot be empty")
    @Column(name = "display_order", nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer displayOrder;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", referencedColumnName = "id", columnDefinition = "BINARY(16)", nullable = false)
    private User lastModifiedBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void normalizeData() {
        this.mainTitle = mainTitle.trim();
        this.secondTitle = secondTitle.trim();
        this.description = description.trim();
    }
}
