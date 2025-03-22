package com.lemurybiznesu.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;
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
@Table(name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email"),
                @Index(name = "idx_user_phone", columnList = "phone_number")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_user_email", columnNames = "email"),
                @UniqueConstraint(name = "uc_user_phone", columnNames = "phone_number")
        })
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    @EqualsAndHashCode.Include
    private UUID id;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50)
    @Pattern(regexp = "^\\p{L}+$", message = "First name must contain only letters")
    @Column(
            name = "first_name",
            nullable = false,
            length = 50,
            columnDefinition = "VARCHAR(50) COLLATE utf8mb4_unicode_ci"
    )
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 80)
    @Pattern(
            regexp = "^[\\p{L}\\s-]+$",
            message = "Last name must contain only letters, spaces or hyphens"
    )
    @Column(
            name = "last_name",
            nullable = false,
            length = 80,
            columnDefinition = "VARCHAR(80) COLLATE utf8mb4_unicode_ci"
    )
    private String lastName;

    @NaturalId
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(
            nullable = false,
            unique = true,
            columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci"
    )
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 60, max = 60)
    @Column(
            nullable = false,
            length = 60,
            columnDefinition = "CHAR(60)"
    )
    private String password;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Invalid phone number format")
    @Column(
            name = "phone_number",
            nullable = false,
            unique = true,
            length = 20,
            columnDefinition = "VARCHAR(20) COLLATE utf8mb4_unicode_ci"
    )
    private String phoneNumber;

    @NotNull(message = "Role cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "role_name",
            referencedColumnName = "name",
            nullable = false,
            columnDefinition = "VARCHAR(20)"
    )
    private Role role;

    @Column(name = "token_version", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT 0")
    private Integer tokenVersion = 0;

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
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.email = email.toLowerCase().trim();
        this.phoneNumber = phoneNumber.replaceAll("[^+0-9]", "");
    }
}
