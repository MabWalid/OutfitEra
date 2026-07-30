package com.outfitera.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Composant utilitaire pour la génération, l'extraction et la validation des jetons JWT.
 * <p>
 * Utilise JJWT 0.12+ avec une clé secrète HMAC-SHA256 de 256 bits encodée en hexadécimal,
 * configurée via les propriétés de l'application pour garantir l'externalisation des secrets.
 * </p>
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    /**
     * Récupère la clé HMAC-SHA256 dérivée du secret hexadécimal configuré.
     *
     * @return La {@link SecretKey} utilisée pour signer et valider les tokens.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Génère un jeton JWT d'accès à partir des informations d'authentification.
     *
     * @param authentication L'objet d'authentification Spring Security.
     * @return Le jeton JWT signé sous forme de chaîne de caractères.
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateTokenFromUserId(userPrincipal.getId());
    }

    /**
     * Génère un jeton JWT d'accès à partir d'un identifiant utilisateur.
     *
     * @param userId L'identifiant numérique de l'utilisateur.
     * @return Le jeton JWT signé.
     */
    public String generateTokenFromUserId(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrait l'identifiant utilisateur (subject) du jeton JWT.
     *
     * @param token Le jeton JWT à analyser.
     * @return L'ID utilisateur encodé dans le sujet du token.
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * Valide un jeton JWT et vérifie sa signature et sa date d'expiration.
     *
     * @param token Le jeton JWT à valider.
     * @return {@code true} si le jeton est valide, {@code false} sinon.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("JWT invalide : {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("JWT expiré : {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("JWT non supporté : {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Les claims JWT sont vides : {}", ex.getMessage());
        }
        return false;
    }
}
