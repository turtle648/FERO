package com.ssafy.api.controller;


import com.ssafy.api.service.UserRankScoresService;
import com.ssafy.common.util.JwtTokenUtil;

import com.ssafy.db.entity.UserRankScores;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 유저 스탯, 레벨, 경험치, 랭크 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 랭크 점수 관련 API", tags = {"UserRankScore"})
@RestController
@RequestMapping("/api/v1/userRankScores")
public class UserRankScoresController {

    private final UserRankScoresService userRankScoresService;

    public UserRankScoresController(UserRankScoresService userRankScoresService) {
        this.userRankScoresService = userRankScoresService;
    }

    @GetMapping
    @ApiOperation(value = "캐릭터 랭크 점수 조회", notes = "특정 운동 또는 전체 운동의 랭크 점수를 확인할 수 있다.")
    public ResponseEntity<?> getCharacterRankScore(HttpServletRequest request,
                                                   @RequestParam(required = false) Long exerciseId) {

        String token = request.getHeader("Authorization");
        String userId = JwtTokenUtil.getUserIdFromJWT(token);

        if (exerciseId != null) {
            UserRankScores rankScore = userRankScoresService.getRankScoreByUserIdAndId(userId, exerciseId);
            if (rankScore == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid exercise type or no data found.");
            }
            return ResponseEntity.ok(rankScore.getRankScore());
        }

        List<UserRankScores> rankScoresList = userRankScoresService.getAllRankScoresByUserId(userId);
        Map<String, Integer> rankScoresMap = new HashMap<>();

        for (UserRankScores rank : rankScoresList) {
            rankScoresMap.put(rank.getExerciseType(), rank.getRankScore().intValue());
        }

        return ResponseEntity.ok(rankScoresMap);
    }
}
