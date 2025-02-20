package com.ssafy.api.service;

import com.ssafy.api.request.EventExerciseLog;
import com.ssafy.api.request.ExerciseLogReq;
import com.ssafy.api.request.ExerciseLogSearchReq;
import com.ssafy.api.response.ExerciseLogRes;
import com.ssafy.api.response.ExerciseStatsRatioRes;
import com.ssafy.api.response.SingleModeRes;
import com.ssafy.db.entity.ExerciseLog;

import java.util.List;

public interface ExerciseLogService {
    // 운동 종목의 스탯 조회
    ExerciseStatsRatioRes getStatsByExerciseLog(Long exerciseStatsRatioId);
    // 운동 기록 추가 및 스탯 업데이트
    ExerciseLog addExerciseLogAndUpdateStats(EventExerciseLog event);
    // 운동 기록 조회 옵션
    List<ExerciseLogRes> searchExerciseLog(
            String userId,
            ExerciseLogSearchReq request
    );
    // 싱글모드 결과 가져오기
    SingleModeRes getSingleModeResult(String userId, EventExerciseLog event);
}
