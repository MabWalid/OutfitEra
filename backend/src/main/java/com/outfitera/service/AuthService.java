package com.outfitera.service;

import com.outfitera.dto.auth.AuthResponse;
import com.outfitera.dto.auth.LoginRequest;
import com.outfitera.dto.auth.RefreshTokenRequest;
import com.outfitera.dto.auth.RegisterRequest;

/**
 * Interface du service d'authentification OutfitEra.
 * <p>
 * Définit le contrat des opérations d'authentification : inscription, connexion,
 * renouvellement de token et déconnexion.
 * </p>
 */
public interface AuthService {

    /**
     * Inscrit un nouvel utilisateur, le persiste en base et retourne les tokens JWT.
     *
     * @param request DTO contenant les données d'inscription.
     * @return Les tokens JWT (access + refresh) et les informations de l'utilisateur.
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authentifie un utilisateur existant et génère de nouveaux tokens JWT.
     *
     * @param request DTO contenant l'identifiant et le mot de passe.
     * @return Les tokens JWT (access + refresh) et les informations de l'utilisateur.
     */
    AuthResponse login(LoginRequest request);

    /**
     * Renouvelle le jeton d'accès à partir d'un refresh token valide.
     *
     * @param request DTO contenant le refresh token.
     * @return Un nouveau jeton d'accès JWT.
     */
    AuthResponse refreshToken(RefreshTokenRequest request);

    /**
     * Révoque le refresh token d'un utilisateur (déconnexion).
     *
     * @param userId L'identifiant de l'utilisateur à déconnecter.
     */
    void logout(Long userId);
}
