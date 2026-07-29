package com.outfitera.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Structure DTO standardisée pour le retour des erreurs de validation et d'exceptions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /** Code d'état HTTP (ex: 400, 404, 500) */
    private int status;

    /** Nom du statut HTTP (ex: BAD_REQUEST, NOT_FOUND) */
    private String error;

    /** Message principal d'erreur */
    private String message;

    /** Chemin de l'URL ayant déclenché l'erreur */
    private String path;

    /** Horodatage exact de l'incident */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /** Détail des erreurs de validation par champ (NomDuChamp -> MessageD'erreur) */
    private Map<String, String> validationErrors;
}
