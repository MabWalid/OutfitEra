package com.outfitera.repository;

import com.outfitera.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository Spring Data JPA pour les opérations CRUD sur l'entité {@link User}.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son email OU son username (pour la connexion multi-identifiant).
     *
     * @param email L'adresse email.
     * @param username Le nom d'utilisateur.
     * @return Un {@link Optional} contenant l'utilisateur si trouvé.
     */
    Optional<User> findByEmailOrUsername(String email, String username);

    /**
     * Recherche un utilisateur par son adresse email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     */
    Optional<User> findByUsername(String username);

    /**
     * Vérifie l'existence d'un utilisateur avec l'email donné.
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie l'existence d'un utilisateur avec le username donné.
     */
    boolean existsByUsername(String username);
}
