package com.outfitera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée principal de l'application OutfitEra Backend.
 * <p>
 * OutfitEra est une plateforme d'essayage virtuel de vêtements enterprise,
 * combinant e-commerce, recommandations IA (Spring AI) et gamification.
 * </p>
 * 
 * @author OutfitEra Team
 * @version 1.0.0
 */
@SpringBootApplication
public class OutfitEraApplication {

    /**
     * Méthode main qui démarre le contexte Spring Boot.
     *
     * @param args arguments de ligne de commande passés à l'application.
     */
    public static void main(String[] args) {
        SpringApplication.run(OutfitEraApplication.class, args);
    }
}
