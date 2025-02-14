package com.ssafy.db.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "friends")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // 요청을 보낸 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;  // 친구가 될 유저

    @Column(name = "friend_nickname")
    private String friendNickname; // 친구의 닉네임 (user_nickname과 연결)

    @Column(name = "friend_level", nullable = false)
    private int friendLevel;  // 기본 레벨 1

    @Column(nullable = false)
    private FriendStatus status;

    @Column(name = "created_at", updatable = false)
    private String createdAt;

//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt = LocalDateTime.now();

//    @PreUpdate
//    protected void onUpdate() {
//        this.updatedAt = LocalDateTime.now();
//    }

    public enum FriendStatus {
        pending, accepted, blocked
    }
}
