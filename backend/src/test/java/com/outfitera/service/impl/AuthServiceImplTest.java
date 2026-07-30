package com.outfitera.service.impl;

import com.outfitera.dto.auth.AuthResponse;
import com.outfitera.dto.auth.LoginRequest;
import com.outfitera.dto.auth.RefreshTokenRequest;
import com.outfitera.dto.auth.RegisterRequest;
import com.outfitera.entity.RefreshToken;
import com.outfitera.entity.User;
import com.outfitera.enums.Role;
import com.outfitera.exception.BusinessException;
import com.outfitera.repository.RefreshTokenRepository;
import com.outfitera.repository.UserRepository;
import com.outfitera.security.JwtTokenProvider;
import com.outfitera.security.UserPrincipal;
import com.outfitera.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du service {@link AuthServiceImpl}.
 * <p>
 * Utilise Mockito pour isoler le service de ses dépendances (repositories, encodeur, JWT).
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthServiceImpl — Tests Unitaires")
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "refreshTokenExpirationMs", 604800000L);

        sampleUser = User.builder()
                .username("walid")
                .email("walid@outfitera.com")
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();
        ReflectionTestUtils.setField(sampleUser, "id", 1L);
    }

    @Test
    @DisplayName("register() — doit inscrire un nouvel utilisateur et retourner les tokens JWT")
    void register_ShouldCreateUserAndReturnTokens() {
        // ARRANGE
        RegisterRequest request = new RegisterRequest(
                "walid", "walid@outfitera.com", "password123", "Walid", "Dev");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        UserPrincipal principal = UserPrincipal.create(sampleUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(tokenProvider.generateToken(any())).thenReturn("accessToken123");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(inv -> inv.getArgument(0));

        // ACT
        AuthResponse response = authService.register(request);

        // ASSERT
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("accessToken123");
        assertThat(response.getEmail()).isEqualTo("walid@outfitera.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("register() — doit lever BusinessException si l'email est déjà utilisé")
    void register_ShouldThrowConflict_WhenEmailExists() {
        // ARRANGE
        RegisterRequest request = new RegisterRequest(
                "walid", "walid@outfitera.com", "password123", null, null);
        when(userRepository.existsByEmail("walid@outfitera.com")).thenReturn(true);

        // ACT & ASSERT
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("walid@outfitera.com");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("refreshToken() — doit lever BusinessException si le token est expiré")
    void refreshToken_ShouldThrowUnauthorized_WhenTokenExpired() {
        // ARRANGE
        RefreshToken expiredToken = RefreshToken.builder()
                .token("expiredToken")
                .user(sampleUser)
                .expiryDate(Instant.now().minusSeconds(3600))
                .build();

        RefreshTokenRequest request = new RefreshTokenRequest("expiredToken");
        when(refreshTokenRepository.findByToken("expiredToken")).thenReturn(Optional.of(expiredToken));

        // ACT & ASSERT
        assertThatThrownBy(() -> authService.refreshToken(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("expiré");
    }
}
