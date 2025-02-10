package com.ssafy.db.repository;

import com.ssafy.db.entity.ExerciseLog;
import com.ssafy.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    // 1. 운동 기록 전체 조회
    List<ExerciseLog> findByUserCharacter_User_UserIdOrderByExerciseDateDesc(String userId);

    // 2. 운동 기록 날짜로 조회 - 이 부분을 수정해야 합니다.
    List<ExerciseLog> findByUserCharacter_User_UserIdAndExerciseDateBetweenOrderByExerciseDateDesc(
            String userId, LocalDateTime startOfDay, LocalDateTime endOfDay
    );

    // 3. 운동 기록 운동 종목별 조회
    List<ExerciseLog> findByUserCharacter_User_UserIdAndExerciseStatsRatio_IdOrderByExerciseDateDesc(
            String userId, Long exerciseStatsRatioId
    );

    // 4. 운동 기록 날짜와 종목별 조회
    List<ExerciseLog> findByUserCharacter_User_UserIdAndExerciseDateBetweenAndExerciseStatsRatio_IdOrderByExerciseDateDesc(
            String userId, LocalDateTime startOfDay, LocalDateTime endOfDay, Long exerciseStatsRatioId
    );
}
