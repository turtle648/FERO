package com.ssafy.db.repository;

import com.ssafy.db.entity.ExerciseStatsRatio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseStatsRatioRepository extends JpaRepository<ExerciseStatsRatio, Long> {
    Optional<ExerciseStatsRatio> findExerciseStatsRatioById(Long exerciseStatsRatioId);

    @Query("SELECT e.id FROM ExerciseStatsRatio e")
    List<Long> findAllExerciseStatsRatioId();
}
