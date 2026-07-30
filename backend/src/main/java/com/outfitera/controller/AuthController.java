package com.outfitera.controller;

import com.outfitera.dto.auth.AuthResponse;
import com.outfitera.dto.auth.LoginRequest;
import com.outfitera.dto.auth.RefreshTokenRequest;
import com.outfitera.dto.auth.RegisterRequest;
import com.outfitera.security.UserPrincipal;
import com.outfitera.service.AuthService;
import com.outfitera.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST gérant toutes les opérations d'authentification de la plateforme OutfitEra.
 * <p>
 * Endpoints publics (ne nécessitent pas de token JWT) :
 * <ul>
 *   <li>POST /auth/register — Inscription</li>
 *   <li>POST /auth/login — Connexion</li>
 *   <li>POST /auth/refresh — Renouvellement de token</li>
 * </ul>
 * Endpoints protégés (nécessitent un token JWT valide) :
 * <ul>
 *   <li>POST /auth/logout — Déconnexion</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints d'inscription, connexion, renouvellement de token JWT et déconnexion")
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint d'inscription d'un nouvel utilisateur.
     * Retourne directement les tokens JWT pour une expérience utilisateur fluide.
     *
     * @param request Les données d'inscription validées.
     * @return HTTP 201 avec les tokens JWT et les informations du compte créé.
     */
    @PostMapping("/register")
    @Operation(summary = "Inscrire un nouvel utilisateur",
            description = "Crée un compte utilisateur et retourne les tokens JWT d'accès directement.")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Compte créé avec succès. Bienvenue sur OutfitEra !", authResponse));
    }

    /**
     * Endpoint de connexion d'un utilisateur existant.
     *
     * @param request L'identifiant (email ou username) et le mot de passe.
     * @return HTTP 200 avec les tokens JWT et les informations de session.
     */
    @PostMapping("/login")
    @Operation(summary = "Connecter un utilisateur existant",
            description = "Accepte email ou username comme identifiant. Retourne un access token (24h) et un refresh token (7j).")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Connexion réussie. Bon shopping !", authResponse));
    }

    /**
     * Endpoint de renouvellement du jeton d'accès JWT sans mot de passe.
     *
     * @param request Le refresh token valide.
     * @return HTTP 200 avec un nouveau jeton d'accès.
     */
    @PostMapping("/refresh")
    @Operation(summary = "Renouveler le jeton d'accès JWT",
            description = "Génère un nouveau access token à partir d'un refresh token encore valide.")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse authResponse = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Jeton d'accès renouvelé avec succès.", authResponse));
    }

    /**
     * Endpoint de déconnexion — révoque le refresh token de l'utilisateur courant.
     *
     * @param currentUser L'utilisateur authentifié extrait du contexte de sécurité.
     * @return HTTP 200 avec confirmation de déconnexion.
     */
    @PostMapping("/logout")
    @Operation(summary = "Déconnecter l'utilisateur courant",
            description = "Révoque le refresh token persisté en base de données.")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        authService.logout(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Déconnexion réussie. À bientôt !"));
    }
}
