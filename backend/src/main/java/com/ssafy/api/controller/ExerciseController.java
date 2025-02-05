package com.ssafy.api.controller;

import com.ssafy.api.request.ExerciseLogReq;
import com.ssafy.api.response.ExerciseLogRes;
import com.ssafy.api.response.ExerciseStatsRatioRes;
import com.ssafy.api.service.ExerciseLogService;
import com.ssafy.api.service.ExerciseLogServiceImpl;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.ExerciseLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 사용자 운동에 대한 기록이나 보상에 대한 API.
 */
@Api(value = "운동 API", tags = {"Exercise"})
@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseLogServiceImpl exerciseService;
    private final ExerciseLogServiceImpl exerciseLogServiceImpl;
    private final ExerciseLogService exerciseLogService;

    @GetMapping("/stats_ratio/{exerciseStatsRatioId}")
    @ApiOperation(value = "운동 종목 별 스탯 조회", notes = "운동 종목의 id로 스탯 비율 조회")
    public ExerciseStatsRatioRes getExerciseStats(@PathVariable Long exerciseStatsRatioId) {
        return exerciseService.getStatsByExerciseLog(exerciseStatsRatioId);
    }

    @PostMapping("/log")
    @ApiOperation(value = "운동 기록 추가", notes = "현재 로그인한 사용자의 운동 기록 추가")
    public ResponseEntity<ExerciseLogRes> addExerciseLog(
            HttpServletRequest request,
            @RequestBody ExerciseLogReq exerciseLogReq) {
        String authHeader = request.getHeader("Authorization");

        // 헤더에서 토큰을 통해 사용자 ID 추출
        String userId = JwtTokenUtil.extractUserIdFromToken(authHeader);

        ExerciseLog savedExerciseLog = exerciseLogService.addExerciseLogAndUpdateStats(
                userId, exerciseLogReq
        );

        return ResponseEntity.ok(ExerciseLogRes.from(savedExerciseLog));
    }
}
