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

    @Column(name = "user_rank", nullable = false, length = 10)
    private String userRank = "-";

    @Column(name = "user_level", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 1 CHECK (user_level <= 100)")
    private Short userLevel = 1;

    @Column(name = "user_arms_status", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (user_arms_status <= 1000)")
    private Short userArmsStatus = 10;

    @Column(name = "user_legs_status", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (user_legs_status <= 1000)")
    private Short userLegsStatus = 10;

    @Column(name = "user_chest_status", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (user_chest_status <= 1000)")
    private Short userChestStatus = 10;

    @Column(name = "user_abs_status", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (user_abs_status <= 1000)")
    private Short userAbsStatus = 10;

    @Column(name = "user_back_status", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (user_back_status <= 1000)")
    private Short userBackStatus = 10;

    @Column(name = "user_stamina_status", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (user_stamina_status <= 1000)")
    private Short userStaminaStatus = 10;

    @Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0 CHECK (points <= 50000)")
    private Short points = 0;
}