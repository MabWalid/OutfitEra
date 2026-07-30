package com.outfitera.enums;

/**
 * Statuts d'exécution de la transaction de paiement.
 */
public enum PaymentStatus {
    /** Transaction initialisée mais non finalisée */
    PENDING,

    /** Transaction réussie et capturée */
    COMPLETED,

    /** Transaction rejetée par la passerelle de paiement */
    FAILED,

    /** Transaction remboursée au client */
    REFUNDED
}
