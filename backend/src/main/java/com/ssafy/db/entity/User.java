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
public class User{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = null;

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    String userId;

    @Column(name = "user_name", nullable = false)
    String userName;

    @Column(name = "user_email", unique = true, nullable = false)
    String userEmail;

    @Column(name = "phone_number", unique = true, nullable = false)
    String phoneNumber;

    @Column(name = "is_valid", columnDefinition = "boolean default true")
    private Boolean isValid;

    @Column(name = "is_temporary_pw", columnDefinition = "boolean default false")
    private Boolean isTemporaryPw;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "user_password", nullable = false)
    String userPassword;

    // 1:1 관계 매핑 추가
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserCharacter userCharacter;
}
