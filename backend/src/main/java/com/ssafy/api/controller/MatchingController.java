package com.ssafy.api.controller;

import com.ssafy.api.request.MatchingReq;
import com.ssafy.api.response.WaitingRoomStatusRes;
import com.ssafy.api.service.MatchingService;
import com.ssafy.db.entity.UserRankScores;
import com.ssafy.db.repository.UserRankScoresRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
@Slf4j
public class MatchingController {
    private final MatchingService matchingService;
    private final UserRankScoresRepository userRankScoresRepository;

    @PostMapping("/enter")
    public ResponseEntity<?> enterWaitingRoom(@RequestBody MatchingReq request) {
//        System.out.println("사용자 : "+request.getUserId() + ", 운동 종류 : "+request.getExerciseType());
        Optional<UserRankScores> userRankscore = userRankScoresRepository.findByUser_UserIdAndExerciseType(
                request.getUserId(),
                request.getExerciseType()
        );

        Short rankScore = userRankscore.map(UserRankScores::getRankScore).orElse((short) 1000);

        System.out.println("사용자 랭크 점수: " + rankScore);

        try {
            // 대기방 입장 처리
            matchingService.enterWaitingRoom(
                    request.getUserId(),
                    request.getExerciseType(),
                    rankScore
            );

            return ResponseEntity.ok().body("대기방 입장 성공");
        } catch (Exception e) {
            log.error("대기방 입장 실패", e);
            return ResponseEntity.badRequest().body("대기방 입장 실패: " + e.getMessage());
        }
    }

    @DeleteMapping("/leave")
    public ResponseEntity<?> leaveWaitingRoom(
            @RequestParam String userId,
            @RequestParam String exerciseType) {
        try {
            matchingService.leaveWaitingRoom(userId, exerciseType);
            return ResponseEntity.ok().body("대기방 퇴장 성공");
        } catch (Exception e) {
            log.error("대기방 퇴장 실패", e);
            return ResponseEntity.badRequest().body("대기방 퇴장 실패: " + e.getMessage());
        }
    }

    // 현재 대기방 상태 조회
    @GetMapping("/status/{exerciseType}")
    public ResponseEntity<?> getWaitingRoomStatus(@PathVariable String exerciseType) {
        try {
            WaitingRoomStatusRes status = matchingService.getWaitingRoomStatus(exerciseType);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            log.error("대기방 상태 조회 실패", e);
            return ResponseEntity.badRequest().body("대기방 상태 조회 실패: " + e.getMessage());
        }
    }
}