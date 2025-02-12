package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "Quests")
public class QuestsEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_character_id")
    private UserCharacter userCharacter;

    @Column(name = "quest_date", nullable = false)
    private LocalDate questDate;

    @Column(name = "quest_time")
    private LocalTime questTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private ExerciseStatsRatio exercise;

    @Column(name = "exercise_cnt", nullable = false)
    private Integer exerciseCnt;

    @Column(name = "real_cnt", nullable = false)
    private Integer realCnt;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @Column(length = 200)
    private String message;

}
