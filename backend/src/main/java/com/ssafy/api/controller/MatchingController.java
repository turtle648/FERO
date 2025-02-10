package com.ssafy.api.controller;

import com.ssafy.api.response.WaitingRoomStatusRes;
import com.ssafy.api.service.MatchingService;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.UserRankScores;
import com.ssafy.db.repository.UserRankScoresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
@Slf4j
public class MatchingController {
    private final MatchingService matchingService;
    private final UserRankScoresRepository userRankScoresRepository;

    // 1. 대기방 입장
    @PostMapping("/enter")
    public ResponseEntity<?> enterWaitingRoom(@RequestParam Long exerciseType,
                                              HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(BaseResponseBody.of(401, "Unauthorized"));
        }
        String accessToken = authHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
        if (!JwtTokenUtil.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponseBody.of(401, "Invalid Access Token"));
        }
        String userId = JwtTokenUtil.getUserIdFromJWT(accessToken);

        System.out.printf("사용자 이름 : %s, 운동 ID : %s\n",userId, exerciseType);

//        System.out.println("사용자 : "+request.getUserId() + ", 운동 종류 : "+request.getExerciseType());
        Optional<UserRankScores> userRankscore = userRankScoresRepository.findByUser_UserIdAndExerciseStatsRatio_Id(
                userId,
                exerciseType
        );

        Short rankScore = userRankscore.map(UserRankScores::getRankScore).orElse((short) 1000);

//        System.out.println("사용자 랭크 점수: " + rankScore);

        try {
            // 대기방 입장 처리
            matchingService.enterWaitingRoom(
                    userId,
                    exerciseType,
                    rankScore
            );

            return ResponseEntity.ok().body("대기방 입장 성공");
        } catch (Exception e) {
            log.error("대기방 입장 실패", e);
            return ResponseEntity.badRequest().body("대기방 입장 실패: " + e.getMessage());
        }
    }

    // 2. 로그인 한 사용자 대기방 나가기
    @DeleteMapping("/leave")
    public ResponseEntity<?> leaveWaitingRoom(
            @RequestParam String userId,
            @RequestParam Long exerciseType) {
        try {
            matchingService.leaveWaitingRoom(userId, exerciseType);
            return ResponseEntity.ok().body("대기방 퇴장 성공");
        } catch (Exception e) {
            log.error("대기방 퇴장 실패", e);
            return ResponseEntity.badRequest().body("대기방 퇴장 실패: " + e.getMessage());
        }
    }
}