package com.ssafy.api.controller;

import com.ssafy.api.response.ExerciseStatsRatioRes;
import com.ssafy.api.service.ExerciseLogService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 사용자 운동에 대한 기록이나 보상에 대한 API.
 */
@Api(value = "운동 API", tags = {"Exercise"})
@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseLogService exerciseService;

    @GetMapping("/stats_ratio/{exerciseStatsRatioId}")
    public ExerciseStatsRatioRes getExerciseStats(@PathVariable Long exerciseStatsRatioId) {
        return exerciseService.getStatsByExerciseLog(exerciseStatsRatioId);
    }
}
