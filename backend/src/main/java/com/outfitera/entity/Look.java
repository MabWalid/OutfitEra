package com.outfitera.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entité représentant une tenue créée dans le module d'Essayage Virtuel.
 * Combines un Haut (Top), un Pantalon (Bottom), des Chaussures (Shoes) et un Accessoire (Accessory).
 */
@Entity
@Table(name = "looks", indexes = {
        @Index(name = "idx_look_user", columnList = "user_id"),
        @Index(name = "idx_look_public_approved", columnList = "is_public, is_approved")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Look extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top_product_id")
    private Product topProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bottom_product_id")
    private Product bottomProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shoes_product_id")
    private Product shoesProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accessory_product_id")
    private Product accessoryProduct;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private boolean isPublic = false;

    @Column(name = "is_approved", nullable = false)
    @Builder.Default
    private boolean isApproved = false;

    /**
     * Calcule automatiquement le prix total du look en additionnant le prix des composants.
     */
    public void calculateTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        if (topProduct != null && topProduct.getPrice() != null) {
            total = total.add(topProduct.getDiscountPrice() != null ? topProduct.getDiscountPrice() : topProduct.getPrice());
        }
        if (bottomProduct != null && bottomProduct.getPrice() != null) {
            total = total.add(bottomProduct.getDiscountPrice() != null ? bottomProduct.getDiscountPrice() : bottomProduct.getPrice());
        }
        if (shoesProduct != null && shoesProduct.getPrice() != null) {
            total = total.add(shoesProduct.getDiscountPrice() != null ? shoesProduct.getDiscountPrice() : shoesProduct.getPrice());
        }
        if (accessoryProduct != null && accessoryProduct.getPrice() != null) {
            total = total.add(accessoryProduct.getDiscountPrice() != null ? accessoryProduct.getDiscountPrice() : accessoryProduct.getPrice());
        }
        this.totalPrice = total;
    }
}
