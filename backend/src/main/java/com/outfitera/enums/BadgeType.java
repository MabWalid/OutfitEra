package com.outfitera.enums;

/**
 * Types de badges et accomplissements pour la gamification.
 */
public enum BadgeType {
    /** Débloqué lors du premier achat réussi */
    FIRST_PURCHASE,

    /** Débloqué après avoir créé 5 looks complets */
    STYLE_MASTER,

    /** Débloqué après avoir publié 10 avis vérifiés */
    REVIEWER,

    /** Débloqué quand un look créé atteint 50 favoris dans la galerie */
    LOOK_CREATOR,

    /** Débloqué pour les utilisateurs influents dans le classement hebdomadaire */
    TOP_TREND
}
