package com.outfitera.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse après une authentification réussie (inscription ou connexion).
 * Contient le jeton d'accès JWT et le refresh token.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    /** Jeton JWT d'accès à durée de vie courte (24h) */
    private String accessToken;

    /** Jeton de rafraîchissement à durée de vie longue (7 jours) */
    private String refreshToken;

    /** Type de token (toujours "Bearer") */
    @Builder.Default
    private String tokenType = "Bearer";

    /** Identifiant de l'utilisateur authentifié */
    private Long userId;

    /** Nom d'utilisateur */
    private String username;

    /** Adresse email */
    private String email;

    /** Rôle de l'utilisateur (ROLE_USER, ROLE_ADMIN) */
    private String role;
}
