package com.outfitera.security;

import com.outfitera.entity.User;
import com.outfitera.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation de {@link UserDetailsService} pour charger les détails
 * d'un utilisateur depuis PostgreSQL lors de l'authentification.
 * <p>
 * Spring Security invoque cette classe automatiquement via le mécanisme
 * d'authentification et de validation des jetons JWT.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Charge un utilisateur par son nom d'utilisateur ou son adresse email.
     * <p>
     * Supporte les deux modes de connexion pour maximiser l'expérience utilisateur.
     * </p>
     *
     * @param identifier Nom d'utilisateur ou adresse email.
     * @return L'objet {@link UserDetails} encapsulant les informations de sécurité.
     * @throws UsernameNotFoundException Si aucun utilisateur ne correspond à l'identifiant.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(identifier, identifier)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur introuvable avec l'identifiant : " + identifier));

        return UserPrincipal.create(user);
    }

    /**
     * Charge un utilisateur directement par son identifiant numérique (ID).
     * Utilisé par le filtre JWT après extraction du sujet du token.
     *
     * @param userId L'identifiant unique de l'utilisateur en base.
     * @return L'objet {@link UserDetails}.
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur introuvable avec l'ID : " + userId));

        return UserPrincipal.create(user);
    }
}
