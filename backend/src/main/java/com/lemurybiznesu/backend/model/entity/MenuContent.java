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
@Table(name = "menu_content",
        indexes = {
            @Index(name = "idx_menu_order", columnList = "display_order"),
            @Index(name = "idx_menu_file_name", columnList = "file_name")
        },
        uniqueConstraints = {
            @UniqueConstraint(name = "uc_menu_order", columnNames = "display_order"),
            @UniqueConstraint(name = "uc_menu_file_name", columnNames = "file_name")
        })
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MenuContent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    @EqualsAndHashCode.Include
    private UUID id;

    @NotBlank(message = "File name cannot be blank")
    @Size(max = 255)
    @Column(name = "file_name", nullable = false, unique = true, length = 255,
            columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    private String fileName;

    @NotNull(message = "Display order cannot be empty")
    @Column(name = "display_order", nullable = false, unique = true, columnDefinition = "INT UNSIGNED")
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
}
