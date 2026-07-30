package com.outfitera.repository;

import com.outfitera.entity.RefreshToken;
import com.outfitera.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository Spring Data JPA pour les opérations CRUD sur l'entité {@link RefreshToken}.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Recherche un refresh token par sa valeur de token.
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Supprime tous les refresh tokens associés à un utilisateur.
     * Utile lors de la déconnexion ou du changement de mot de passe.
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteByUser(User user);
}
