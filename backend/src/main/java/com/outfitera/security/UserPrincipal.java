package com.outfitera.security;

import com.outfitera.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adaptateur Spring Security qui encapsule une entité {@link User} du domaine
 * et implémente l'interface {@link UserDetails}.
 * <p>
 * C'est cet objet qui est stocké dans le contexte de sécurité Spring
 * et que le filtre JWT utilisera pour identifier l'utilisateur courant.
 * </p>
 */
@Getter
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructeur privé — utiliser {@link #create(User)} pour instancier.
     */
    private UserPrincipal(Long id, String username, String email,
                          String password, boolean enabled,
                          Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    /**
     * Fabrique un {@link UserPrincipal} à partir d'une entité utilisateur du domaine.
     *
     * @param user L'entité {@link User} chargée depuis la base de données.
     * @return L'instance de {@link UserPrincipal} prête pour Spring Security.
     */
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                authorities
        );
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
