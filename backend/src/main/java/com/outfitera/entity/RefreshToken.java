package com.outfitera.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Entité représentant un jeton de rafraîchissement JWT (Refresh Token) pour l'authentification sécurisée.
 */
@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_refresh_token", columnList = "token", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    /**
     * Vérifie si le jeton a expiré.
     *
     * @return true si le token est encore valide, false s'il a expiré.
     */
    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }
}
