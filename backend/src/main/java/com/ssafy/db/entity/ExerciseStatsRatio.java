package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Table(name = "exercise_stats_ratio")
public class ExerciseStatsRatio extends BaseEntity {
    @Column(name = "exercise_type", nullable = false, length = 50)
    private String exerciseType;

    @Column(name = "chest_ratio", nullable = false, precision = 5, scale = 2)
    private Float chestRatio;

    @Column(name = "back_ratio", nullable = false, precision = 5, scale = 2)
    private Float backRatio;

    @Column(name = "stamina_ratio", nullable = false, precision = 5, scale = 2)
    private Float staminaRatio;

    @Column(name = "arms_ratio", nullable = false, precision = 5, scale = 2)
    private Float armsRatio;

    @Column(name = "legs_ratio", nullable = false, precision = 5, scale = 2)
    private Float legsRatio;

    @Column(name = "abs_ratio", nullable = false, precision = 5, scale = 2)
    private Float absRatio;
}

