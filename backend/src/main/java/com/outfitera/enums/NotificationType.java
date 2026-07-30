package com.outfitera.enums;

/**
 * Types de notifications système envoyées aux utilisateurs.
 */
public enum NotificationType {
    /** Mise à jour du statut d'une commande (expédiée, livrée) */
    ORDER_STATUS,

    /** Déverrouillage d'un nouveau badge de récompense */
    BADGE_UNLOCKED,

    /** Validation d'un look publié dans la galerie publique par un admin */
    LOOK_APPROVED,

    /** Réception d'un nouvel avis ou mention */
    REVIEW_RECEIVED
}
