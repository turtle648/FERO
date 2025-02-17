package com.ssafy.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResult extends BaseEntity {

    @Column(nullable = false)
    private String gameId; // 경기 ID (UUID 추천)

    @Column(nullable = false)
    private Integer duration; // 경기 시간

    @Column(nullable = false)
    private Long exerciseId; // 운동 종류

    @Column(nullable = false)
    private String userId; // 유저 ID

    @Column(nullable = false)
    private String opponentId; // 상대방 ID

    @Column(nullable = false)
    private short userScore; // 유저 점수

    @Column(nullable = false)
    private short opponentScore; // 상대 점수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameResultType result; // 승패 결과 (WIN, LOSE, DRAW)

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt; // 생성 시간

    public enum GameResultType {
        WIN, LOSE, DRAW
    }
}
