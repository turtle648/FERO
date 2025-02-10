package com.ssafy.db.repository;

import com.ssafy.db.entity.UserRankScores;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRankScoresRepository extends JpaRepository<UserRankScores, Long> {
    List<UserRankScores> findByUser_UserId(String userId);  // 특정 유저의 모든 운동 랭크 점수 조회

    Optional<UserRankScores> findByUser_UserIdAndId(String userUserId, Long id);
    // 특정 유저의 특정 운동 랭크 점수 가져오기
    Optional<UserRankScores> findByUser_UserIdAndExerciseType(String userUserId, String exerciseType);
}
