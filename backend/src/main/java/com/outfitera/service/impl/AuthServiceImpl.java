package com.outfitera.service.impl;

import com.outfitera.dto.auth.AuthResponse;
import com.outfitera.dto.auth.LoginRequest;
import com.outfitera.dto.auth.RefreshTokenRequest;
import com.outfitera.dto.auth.RegisterRequest;
import com.outfitera.entity.RefreshToken;
import com.outfitera.entity.User;
import com.outfitera.exception.BusinessException;
import com.outfitera.exception.ResourceNotFoundException;
import com.outfitera.repository.RefreshTokenRepository;
import com.outfitera.repository.UserRepository;
import com.outfitera.security.JwtTokenProvider;
import com.outfitera.security.UserPrincipal;
import com.outfitera.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Implémentation concrète du service d'authentification OutfitEra.
 * <p>
 * Orchestre l'inscription, la connexion, le rafraîchissement de tokens et la déconnexion
 * en combinant Spring Security, JJWT et Spring Data JPA.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpirationMs;

    /**
     * {@inheritDoc}
     * <p>
     * Vérifie l'unicité de l'email et du username avant de persister le nouvel utilisateur.
     * </p>
     */
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Vérification unicité email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(
                    "Un compte existe déjà avec l'email : " + request.getEmail(),
                    HttpStatus.CONFLICT);
        }

        // Vérification unicité username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(
                    "Un compte existe déjà avec le nom d'utilisateur : " + request.getUsername(),
                    HttpStatus.CONFLICT);
        }

        // Création et persistance de l'utilisateur
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        userRepository.save(user);
        log.info("Nouvel utilisateur inscrit : {} ({})", user.getUsername(), user.getEmail());

        // Authentification automatique après inscription
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        return buildAuthResponse(authentication, user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", userPrincipal.getId()));

        log.info("Connexion réussie pour l'utilisateur : {}", user.getUsername());
        return buildAuthResponse(authentication, user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BusinessException("Refresh token introuvable", HttpStatus.UNAUTHORIZED));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new BusinessException("Refresh token expiré. Veuillez vous reconnecter.", HttpStatus.UNAUTHORIZED);
        }

        User user = refreshToken.getUser();
        String newAccessToken = tokenProvider.generateTokenFromUserId(user.getId());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken.getToken())
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", userId));

        refreshTokenRepository.deleteByUser(user);
        SecurityContextHolder.clearContext();
        log.info("Déconnexion réussie pour l'utilisateur ID : {}", userId);
    }

    /**
     * Construit le DTO {@link AuthResponse} avec les tokens JWT générés.
     *
     * @param authentication L'authentification Spring Security.
     * @param user L'entité utilisateur.
     * @return Le DTO de réponse d'authentification.
     */
    private AuthResponse buildAuthResponse(Authentication authentication, User user) {
        String accessToken = tokenProvider.generateToken(authentication);
        RefreshToken refreshToken = createOrUpdateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    /**
     * Crée ou remplace le refresh token persisté pour un utilisateur.
     * <p>
     * Supprime l'ancien token avant d'en créer un nouveau (rotation de tokens).
     * </p>
     *
     * @param user L'utilisateur pour lequel générer un refresh token.
     * @return L'entité {@link RefreshToken} persistée.
     */
    private RefreshToken createOrUpdateRefreshToken(User user) {
        // Suppression de l'ancien refresh token (rotation)
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
}
