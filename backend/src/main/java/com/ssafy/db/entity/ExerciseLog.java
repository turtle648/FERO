package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.security.Timestamp;

@Entity
@Table(name = "exercise_log")
@Getter
@Setter
public class ExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "exercise_duration", nullable = false)
    private Integer exerciseDuration;

    @Column(name = "exercise_cnt", nullable = false)
    private Integer exerciseCnt;

    @ManyToOne
    @JoinColumn(name = "exercise_stats_ratio_id", nullable = false)
    private ExerciseStatsRatio exerciseStatsRatio;

    @Column(name = "exercise_date", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp exerciseDate;
}

