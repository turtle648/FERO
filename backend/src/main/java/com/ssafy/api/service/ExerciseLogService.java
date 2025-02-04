package com.ssafy.api.service;

import com.ssafy.api.response.ExerciseStatsRatioRes;
import com.ssafy.db.repository.ExerciseStatsRatioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExerciseLogService {
    private final ExerciseStatsRatioRepository exerciseStatsRatioRepository;

    public ExerciseStatsRatioRes getStatsByExerciseLog(Long exerciseStatsRatioId) {
        return exerciseStatsRatioRepository.findById(exerciseStatsRatioId)
                .map(ExerciseStatsRatioRes::of)
                .orElseThrow(() -> new RuntimeException("해당 ID의 운동 통계를 찾을 수 없습니다."));
    }
}
