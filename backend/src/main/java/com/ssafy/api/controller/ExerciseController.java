package com.ssafy.api.controller;

import com.ssafy.api.request.EventExerciseLog;
import com.ssafy.api.request.ExerciseLogReq;
import com.ssafy.api.request.ExerciseLogSearchReq;
import com.ssafy.api.response.*;
import com.ssafy.api.service.ExerciseLogService;
import com.ssafy.api.service.ExerciseLogServiceImpl;
import com.ssafy.api.service.QuestsService;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.ExerciseLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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
    private final QuestsService questService;

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
        String userId = JwtTokenUtil.getUserIdFromJWT(authHeader);

        ExerciseLog savedExerciseLog = exerciseLogService.addExerciseLogAndUpdateStats(
                new EventExerciseLog(userId, exerciseLogReq)
        );

        return ResponseEntity.ok(ExerciseLogRes.from(savedExerciseLog));
    }

    @GetMapping("/logs")
    @ApiOperation(value = "운동 기록 조회", notes = "사용자의 운동 기록을 다양한 조건으로 조회")
    public ResponseEntity<List<ExerciseLogRes>> searchExerciseLogs(
            HttpServletRequest request,
            @RequestParam(required = false) Long exerciseStatsRatioId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate
    ) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }

        try {
            String userId = JwtTokenUtil.getUserIdFromJWT(authHeader);
            System.out.println("조회 대상 사용자 ID : " + userId);

            // DTO 대신 파라미터 직접 전달
            ExerciseLogSearchReq searchOption = new ExerciseLogSearchReq();
            searchOption.setExerciseStatsRatioId(exerciseStatsRatioId);
            searchOption.setSearchDate(searchDate);

            List<ExerciseLogRes> exerciseLogRes = exerciseLogService.searchExerciseLog(
                    userId, searchOption
            );
            return ResponseEntity.ok(exerciseLogRes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
    }

    @GetMapping("/monthly")
    @ApiOperation(value = "월별 퀘스트 달성 현황", notes = "특정 연월의 퀘스트 달성 현황을 조회")
    public ResponseEntity<List<MonthlyQuestsStatusRes>> getMonthlyQuests(
            HttpServletRequest request,
            @ApiParam(value = "조회할 연도", required = true) @RequestParam int year,
            @ApiParam(value = "조회할 월", required = true) @RequestParam int month
    ) {
        String userId = JwtTokenUtil.getUserIdFromJWT(request.getHeader("Authorization"));
        return questService.getMonthlyQuestStatus(userId, year, month)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/today")
    @ApiOperation(value = "오늘의 퀘스트 조회", notes = "현재 로그인한 사용자의 오늘 퀘스트 정보를 조회")
    public ResponseEntity<List<QuestsRes>> getTodayQuests(HttpServletRequest request) {
        String userId = JwtTokenUtil.getUserIdFromJWT(request.getHeader("Authorization"));

        return questService.getTodayQuest(userId, LocalDate.now())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/single-mode")
    @ApiOperation(value = "싱글 모드 결과 가져오기", notes = "싱글모드로 스탯, 레벨, 경험치의 전 후 값")
    public ResponseEntity<SingleModeRes> getSingleModeResult(
            HttpServletRequest request,
            @RequestBody ExerciseLogReq exerciseLogReq) {

        String authHeader = request.getHeader("Authorization");

        // 헤더에서 토큰을 통해 사용자 ID 추출
        String userId = JwtTokenUtil.getUserIdFromJWT(authHeader);

        SingleModeRes result = exerciseLogService.getSingleModeResult(
                userId, new EventExerciseLog(userId, exerciseLogReq));
        return ResponseEntity.ok(result);
    }
}
