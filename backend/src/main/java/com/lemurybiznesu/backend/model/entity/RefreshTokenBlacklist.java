package com.lemurybiznesu.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens_blacklist",
        indexes = {
                @Index(name = "idx_blacklist_token", columnList = "token"),
                @Index(name = "idx_blacklist_expiry", columnList = "expiryTime")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uc_blacklist_token", columnNames = "token")
        })
@EntityListeners(AuditingEntityListener.class)
public class RefreshTokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 100, max = 700)
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(700)")
    private String token;

    @NotNull
    @Future
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Date expiryTime;

    @CreatedDate
    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
