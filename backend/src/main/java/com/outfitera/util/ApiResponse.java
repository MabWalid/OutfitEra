package com.outfitera.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Wrapper de réponse REST universel pour l'ensemble de l'API OutfitEra.
 * <p>
 * Standardise la structure JSON de toutes les réponses HTTP renvoyées au client frontend (Angular).
 * </p>
 *
 * @param <T> Type de la donnée encapsulée dans le corps de la réponse.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /** Statut du succès de la requête (true si 2xx, false en cas d'erreur) */
    private boolean success;

    /** Message descriptif destiné aux utilisateurs ou aux développeurs frontend */
    private String message;

    /** Données métier renvoyées par le service */
    private T data;

    /** Horodatage exact de l'émission de la réponse */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Fabrique un objet ApiResponse indiquant un succès sans données retournées.
     *
     * @param message Message de succès.
     * @param <T> Type générique.
     * @return Instance d'ApiResponse configurée pour le succès.
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Fabrique un objet ApiResponse indiquant un succès avec payload de données.
     *
     * @param message Message de succès.
     * @param data Contenu métier de la réponse.
     * @param <T> Type générique.
     * @return Instance d'ApiResponse configurée pour le succès avec données.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Fabrique un objet ApiResponse indiquant un échec.
     *
     * @param message Message décrivant l'erreur.
     * @param <T> Type générique.
     * @return Instance d'ApiResponse configurée pour l'erreur.
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
