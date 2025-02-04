package com.ssafy.db.repository;

import com.ssafy.db.entity.ExerciseStatsRatio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseStatsRatioRepository extends JpaRepository<ExerciseStatsRatio, Long> {
}
