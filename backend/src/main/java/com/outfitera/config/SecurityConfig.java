package com.outfitera.config;

import com.outfitera.security.CustomUserDetailsService;
import com.outfitera.security.JwtAuthenticationEntryPoint;
import com.outfitera.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration principale de Spring Security 6 pour OutfitEra.
 * <p>
 * Architecture Stateless (sans session HTTP) basée sur JWT.
 * Utilise la nouvelle API déclarative {@link SecurityFilterChain}
 * (l'ancienne {@code WebSecurityConfigurerAdapter} est obsolète depuis Spring Security 5.7+).
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Active @PreAuthorize / @PostAuthorize dans les contrôleurs
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Endpoints publics accessibles sans authentification JWT.
     */
    private static final String[] PUBLIC_URLS = {
            "/auth/**",
            "/health",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    /**
     * Bean principal définissant la chaîne de filtres de sécurité HTTP.
     *
     * @param http Le constructeur de configuration Spring Security.
     * @return La chaîne de filtres de sécurité configurée.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Désactiver CSRF (inutile en mode stateless JWT)
                .csrf(AbstractHttpConfigurer::disable)

                // Définir le gestionnaire d'erreur 401 personnalisé
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler))

                // Mode STATELESS : aucune session HTTP côté serveur
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Règles d'autorisation des endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .anyRequest().authenticated())

                // Fournisseur d'authentification
                .authenticationProvider(authenticationProvider())

                // Insertion du filtre JWT AVANT le filtre d'authentification par formulaire
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    /**
     * Fournisseur DAO d'authentification reliant UserDetailsService et PasswordEncoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean du gestionnaire d'authentification exposé pour être utilisé dans AuthService.
     *
     * @param authConfig La configuration d'authentification Spring.
     * @return L'{@link AuthenticationManager}.
     * @throws Exception En cas d'erreur de configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Encodeur de mot de passe BCrypt avec le facteur de coût par défaut (10 rounds).
     *
     * @return L'instance de {@link BCryptPasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
