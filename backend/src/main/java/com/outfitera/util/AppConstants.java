package com.outfitera.util;

/**
 * Constantes globales de l'application OutfitEra.
 * <p>
 * Centralise les valeurs d'en-têtes, de rôles, de pagination et de répertoires
 * pour éviter les littéraux magiques dispersés dans le code (Code Smells).
 * </p>
 */
public final class AppConstants {

    private AppConstants() {
        // Empêche l'instanciation de la classe utilitaire
    }

    // Pagination par défaut
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "createdAt";
    public static final String DEFAULT_SORT_DIRECTION = "desc";

    // Sécurité & En-têtes HTTP
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    // Rôles Utilisateurs
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    // Stockage & Répertoires Images
    public static final String UPLOAD_DIR = "uploads/";
}
