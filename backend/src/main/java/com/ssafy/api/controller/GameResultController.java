package com.ssafy.api.controller;

import com.ssafy.api.request.UserIdGameResultReq;
import com.ssafy.api.response.GameResultsRes;
import com.ssafy.api.service.GameResultServiceImpl;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.GameResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/api/gameResults")
@RequiredArgsConstructor
public class GameResultController {

    private final GameResultServiceImpl gameResultService;

    @PostMapping("/save")
    @ApiOperation(value = "게임 결과 저장", notes = "두 유저의 경기 결과를 저장한다.")
    public ResponseEntity<String> saveGameResult(@RequestBody UserIdGameResultReq request) {
        gameResultService.saveGameResult(request);
        return ResponseEntity.ok("Game result saved successfully");
    }

    @GetMapping
    @ApiOperation(value = "유저 경기 전적 조회", notes = "특정 유저의 최근 경기 전적을 조회한다.")
    public ResponseEntity<List<GameResultsRes>> getUserGameRecords(HttpServletRequest request) {
        String userId = JwtTokenUtil.getUserIdFromJWT(request.getHeader("Authorization"));

        return ResponseEntity.ok(gameResultService.getUserGameRecords(userId));
    }
}
