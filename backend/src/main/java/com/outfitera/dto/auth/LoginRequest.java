package com.outfitera.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requête pour la connexion d'un utilisateur existant.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "L'identifiant (email ou username) est obligatoire")
    private String identifier; // Accepte email OU username

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
