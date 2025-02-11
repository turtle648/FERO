package com.ssafy.db.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_stats")
@JsonIgnoreProperties({"user"})
public class UserStats extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "arms_stats", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 10")
    private Short armsStats = 10;

    @Column(name = "legs_stats", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 10")
    private Short legsStats = 10;

    @Column(name = "chest_stats", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 10")
    private Short chestStats = 10;

    @Column(name = "abs_stats", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 10")
    private Short absStats = 10;

    @Column(name = "back_stats", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 10")
    private Short backStats = 10;

    @Column(name = "stamina_stats", nullable = false, columnDefinition = "SMALLINT UNSIGNED DEFAULT 10")
    private Short staminaStats = 10;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}