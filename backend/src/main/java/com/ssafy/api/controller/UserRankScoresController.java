package com.ssafy.api.controller;


import com.ssafy.api.request.ExerciseResultEvent;
import com.ssafy.api.request.RankUpdateReq;
import com.ssafy.api.service.UserRankScoresService;
import com.ssafy.common.util.JwtTokenUtil;

import com.ssafy.api.response.RankUpdateRes;
import com.ssafy.db.entity.UserRankScores;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            rankScoresMap.put(rank.getExerciseStatsRatio().getExerciseType(), rank.getRankScore().intValue());
        }

        return ResponseEntity.ok(rankScoresMap);
    }

    @PostMapping("/update")
    @ApiOperation(value = "랭크 점수 업데이트", notes = "Elo 시스템을 적용하여 승패에 따라 점수를 조정한다. \n (result: user1이 이겼을 때 1, 졌을 때 2, 비겼을 때 0)")
    public ResponseEntity<RankUpdateRes> updateRank(@RequestBody ExerciseResultEvent exerciseResultEvent) {

//        String user1Id = rankUpdateReq.getUser1Id();
//        String user2Id = rankUpdateReq.getUser2Id();
//        Long exerciseId = rankUpdateReq.getExerciseId();
//        int result =rankUpdateReq.getResult();

        try {
            RankUpdateRes response = userRankScoresService.updateRankScore(exerciseResultEvent);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
