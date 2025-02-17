package com.ssafy.db.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_stats_history")
public class UserStatsHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_character_id")
    private UserCharacter userCharacter;

    @Column(name = "arms_stats", nullable = false)
    private Integer armsStats;

    @Column(name = "legs_stats", nullable = false)
    private Integer legsStats;

    @Column(name = "chest_stats", nullable = false)
    private Integer chestStats;

    @Column(name = "abs_stats", nullable = false)
    private Integer absStats;

    @Column(name = "back_stats", nullable = false)
    private Integer backStats;

    @Column(name = "stamina_stats", nullable = false)
    private Integer staminaStats;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "stats_date", nullable = false)
    private LocalDate statsDate;
}
