package com.ssafy.db.repository;

import com.ssafy.db.entity.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Integer> {
    // 운동 기록 추가
}
