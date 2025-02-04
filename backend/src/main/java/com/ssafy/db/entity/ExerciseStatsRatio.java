package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "exercise_stats_ratio")
public class ExerciseStatsRatio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exercise_type", nullable = false)
    private String exerciseType;

    @Column(name = "chest_ratio", nullable = false)
    private BigDecimal chestRatio;

    @Column(name = "back_ratio", nullable = false)
    private BigDecimal backRatio;

    @Column(name = "stamina_ratio", nullable = false)
    private BigDecimal staminaRatio;

    @Column(name = "arms_ratio", nullable = false)
    private BigDecimal armsRatio;

    @Column(name = "legs_ratio", nullable = false)
    private BigDecimal legsRatio;

    @Column(name = "abs_ratio", nullable = false)
    private BigDecimal absRatio;
}

