package com.ssafy.db.repository;

import com.ssafy.db.entity.UserRankScores;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserRankScoresRepository extends JpaRepository<UserRankScores, Long> {
    List<UserRankScores> findByUser_UserId(String userId);  // 특정 유저의 모든 운동 랭크 점수 조회

    Optional<UserRankScores> findByUser_UserIdAndId(String userUserId, Long id);
    // 특정 유저의 특정 운동 랭크 점수 가져오기
    Optional<UserRankScores> findByUser_UserIdAndExerciseType(String userUserId, String exerciseType);

    Optional<UserRankScores> findByUser_UserIdAndExerciseStatsRatio_Id(String userUserId, Long id);

    @Modifying
    @Transactional
    @Query("UPDATE UserRankScores ur SET ur.rankScore = ur.rankScore + :rankScore " +
            "WHERE ur.user.userId = :userId AND ur.exerciseStatsRatio.id = :exerciseId")
    void updateUserRankScore(
            @Param("userId") String userId,
            @Param("exerciseId") Long exerciseId,
            @Param("rankScore") short rankScore
    );

}
