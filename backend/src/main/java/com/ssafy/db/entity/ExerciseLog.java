package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "exercise_log")
public class ExerciseLog extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserCharacter userCharacter;

    @Column(name = "exercise_duration", nullable = false)
    private Integer exerciseDuration;

    @Column(name = "exercise_cnt", nullable = false)
    private Integer exerciseCount;

    @ManyToOne
    @JoinColumn(name = "exercise_stats_ratio_id", nullable = false)
    private ExerciseStatsRatio exerciseStatsRatio;

    @Column(name = "exercise_date")
    private LocalDateTime exerciseDate;
}