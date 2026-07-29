package com.outfitera.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration OpenAPI 3.0 (Swagger) pour documenter la plateforme OutfitEra.
 * <p>
 * Inclut la définition du schéma d'authentification Bearer JWT pour sécuriser
 * et tester les endpoints directement depuis Swagger UI.
 * </p>
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    /**
     * Bean définissant les métadonnées OpenAPI et le schéma de sécurité JWT.
     *
     * @return L'instance configurée d'{@link OpenAPI}.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OutfitEra REST API")
                        .version("1.0.0")
                        .description("API REST Enterprise pour la plateforme d'essayage virtuel OutfitEra. " +
                                "Intègre E-commerce, IA (Spring AI), Gamification et Authentification JWT.")
                        .contact(new Contact()
                                .name("OutfitEra Engineering Team")
                                .email("contact@outfitera.com")
                                .url("https://github.com/MabWalid/OutfitEra"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Entrez le jeton JWT avec le préfixe Bearer. Exemple: 'Bearer eyJhbGci...'")));
    }
}
