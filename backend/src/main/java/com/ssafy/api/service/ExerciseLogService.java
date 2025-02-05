package com.ssafy.api.service;

import com.ssafy.api.request.ExerciseLogReq;
import com.ssafy.api.response.ExerciseStatsRatioRes;
import com.ssafy.db.entity.ExerciseLog;

public interface ExerciseLogService {
    // 운동 종목의 스탯 조회
    ExerciseStatsRatioRes getStatsByExerciseLog(Long exerciseStatsRatioId);
    // 운동 기록 추가 및 스탯 업데이트
    ExerciseLog addExerciseLogAndUpdateStats(String userId, ExerciseLogReq request);
}
