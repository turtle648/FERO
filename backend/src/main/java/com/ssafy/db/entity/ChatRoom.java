package com.ssafy.db.entity;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id_1", "user_id_2"})})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id_1", nullable = false)
    private String userId1;

    @Column(name = "user_id_2", nullable = false)
    private String userId2;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}