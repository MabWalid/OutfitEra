package com.outfitera.enums;

/**
 * Statuts de traitement d'une commande client dans OutfitEra.
 */
public enum OrderStatus {
    /** Commande créée en attente de paiement */
    PENDING,

    /** Commande payée et confirmée */
    PAID,

    /** Commande en cours de préparation en entrepôt */
    PROCESSING,

    /** Commande expédiée et en cours d'acheminement */
    SHIPPED,

    /** Commande livrée au client */
    DELIVERED,

    /** Commande annulée par le client ou le système */
    CANCELLED
}
