package com.ssafy.api.response;

import com.ssafy.db.entity.ExerciseLog;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExerciseLogRes {
    private Long id;
    private String userId;
    private Integer exerciseDuration;
    private Integer exerciseCount;
    private Long exerciseStatsRatioId;
    private LocalDateTime exerciseDate;

    public static ExerciseLogRes from(ExerciseLog exerciseLog) {
        ExerciseLogRes response = new ExerciseLogRes();
        response.setId(exerciseLog.getId());
        response.setUserId(exerciseLog.getUserCharacter().getUser().getUserId());
        response.setExerciseDuration(exerciseLog.getExerciseDuration());
        response.setExerciseCount(exerciseLog.getExerciseCount());
        response.setExerciseStatsRatioId(exerciseLog.getExerciseStatsRatio().getId());
        response.setExerciseDate(exerciseLog.getExerciseDate());
        return response;
    }
}
