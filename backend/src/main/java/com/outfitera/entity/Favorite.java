package com.outfitera.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant la mise en favori d'un produit ou d'un look par un utilisateur.
 */
@Entity
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "product_id"}),
        @UniqueConstraint(columnNames = {"user_id", "look_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "look_id")
    private Look look;
}
