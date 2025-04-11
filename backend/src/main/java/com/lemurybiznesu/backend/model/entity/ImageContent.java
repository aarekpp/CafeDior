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
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "images_content",
        indexes = {
            @Index(name = "idx_image_content_type", columnList = "image_content_type"),
            @Index(name = "idx_image_filename", columnList = "filename")
        },
        uniqueConstraints = {
            @UniqueConstraint(name = "idx_image_content_type", columnNames = "image_content_type"),
            @UniqueConstraint(name = "idx_image_filenaame", columnNames = "filename")
        })
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ImageContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String filename;

    @NotBlank
    @Size(max = 400)
    @Column(name = "original_filename", nullable = false, length = 400, columnDefinition = "VARCHAR(400) COLLATE utf8mb4_unicode_ci")
    private String originalFilename;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "image_content_type", nullable = false, columnDefinition = "VARCHAR(20)")
    private EImageContent imageContentType;

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
    private void normalizeData(){
        this.originalFilename = originalFilename.trim();
    }
}
