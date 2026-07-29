package com.outfitera.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration Spring Data JPA.
 * <p>
 * Active le système d'Audit JPA automatique (@CreatedDate, @LastModifiedDate, @CreatedBy, @LastModifiedBy)
 * pour assurer une traçabilité complète sur toutes les entités de la base de données.
 * </p>
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
