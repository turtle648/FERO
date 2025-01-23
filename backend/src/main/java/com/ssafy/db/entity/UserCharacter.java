package com.ssafy.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 유저 모델 정의.
 */
@Entity
@Getter
@Setter
@Table(name = "user_character")
public class UserCharacter extends BaseEntity{

//    @Column(name ="user_id", unique = true, nullable = false)
//    private String userId;

    @Column(nullable = false)
    private char gender;

    @Column(name = "user_nickname", unique = true, nullable = false)
    private String userNickname;

    @Column(name = "pushup_best_record", nullable = false)
    private Short pushupRecord;

    @Column(name = "squat_best_record", nullable = false)
    private Short squatRecord;

    @Column(name = "pullup_best_record", nullable = false)
    private Short pullupRecord;

    @Column(nullable = false)
    private Short points;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User userInfo;
}
