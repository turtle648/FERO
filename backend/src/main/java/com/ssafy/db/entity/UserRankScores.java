package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(
        name = "user_rank_scores",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "exercise_type"}
                )
        }
)
public class UserRankScores extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "exercise_type", nullable = false)
    private String exerciseType;

    @Column(name = "rank_score", nullable = false)
    private Short rankScore = 1000; // 기본값 1000 설정
}