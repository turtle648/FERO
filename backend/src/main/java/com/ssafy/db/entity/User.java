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
@Access(AccessType.FIELD)
@Getter
@Setter
@Table(name = "user_info")
public class User extends BaseEntity{
    @Column(name = "user_id", unique = true, nullable = false)
    String userId;

    @Column(name = "user_name", nullable = false)
    String userName;

    @Column(name = "user_email", unique = true, nullable = false)
    String userEmail;

    @Column(name = "phone_number", unique = true, nullable = false)
    String phoneNumber;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "user_password", nullable = false)
    String userPassword;

    // 1:1 관계 매핑 추가
<<<<<<< HEAD
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserCharacter userCharacter;
=======
    @OneToOne(mappedBy = "userInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    UserCharacter userCharacter;
>>>>>>> 906ece2 (fix(docker): redis 통합 완료 - #S12P11E103-303)
}
