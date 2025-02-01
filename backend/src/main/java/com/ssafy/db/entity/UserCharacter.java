package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 유저 캐릭터 모델 정의.
 */
@Entity
@Getter
@Setter
@Table(name = "user_character")
public class UserCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = null;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "user_nickname", unique = true, nullable = false, length = 15)
    private String userNickname;

    @Column(nullable = false)
    private char gender;

    @Column(name = "pushup_best_record", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (pushup_best_record <= 1000)")
    private Short pushupRecord;

    @Column(name = "squat_best_record", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (squat_best_record <= 1000)")
    private Short squatRecord;

    @Column(name = "pullup_best_record", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (pullup_best_record <= 1000)")
    private Short pullupRecord;

    @Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (points <= 50000)")
    private Short points;
}