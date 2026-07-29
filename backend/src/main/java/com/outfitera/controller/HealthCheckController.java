package com.outfitera.controller;

import com.outfitera.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Contrôleur de vérification de l'état de santé du service OutfitEra Backend.
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health Check", description = "Endpoint de test et de diagnostic de l'état du serveur")
public class HealthCheckController {

    /**
     * Endpoint public permettant de s'assurer que le service est en ligne et fonctionnel.
     *
     * @return Informations de statut de l'application (UP, version, horodatage).
     */
    @GetMapping
    @Operation(summary = "Vérifier le statut de santé du serveur API", description = "Retourne l'état 'UP' et les métadonnées de l'application.")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkHealth() {
        Map<String, Object> healthInfo = Map.of(
                "status", "UP",
                "application", "OutfitEra Backend",
                "version", "1.0.0",
                "javaVersion", System.getProperty("java.version"),
                "timestamp", System.currentTimeMillis()
        );
        return ResponseEntity.ok(ApiResponse.success("Le service OutfitEra Backend fonctionne parfaitement", healthInfo));
    }
}
