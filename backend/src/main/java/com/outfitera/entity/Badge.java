package com.outfitera.entity;

import com.outfitera.enums.BadgeType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant un badge de gamification et récompense déverrouillable.
 */
@Entity
@Table(name = "badges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon_url")
    private String iconUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "badge_type", nullable = false, unique = true, length = 30)
    private BadgeType badgeType;

    @Column(name = "points_required", nullable = false)
    @Builder.Default
    private Integer pointsRequired = 0;
}
