package com.ssafy.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 유저 캐릭터 모델 정의.
 */
@Entity
@Getter
@Setter
@Table(name = "user_character")
@JsonIgnoreProperties("user")  // 🔥 user 필드는 JSON 변환 시 제외
public class UserCharacter extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "user_nickname", unique = true, nullable = false, length = 15)
    private String userNickname;

    @Column(nullable = false)
    private char gender;

    @Column(name = "user_level", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 1")
    private Short userLevel = 1;

    @Column(name = "user_experience", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer userExperience = 0;

    @Column(nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 0")
    private Short points = 0;

    @OneToMany(mappedBy = "userCharacter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseLog> exerciseLogs;
}