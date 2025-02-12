package com.ssafy.api.service;

import com.ssafy.api.request.ExerciseLogReq;
import com.ssafy.api.request.ExerciseLogSearchReq;
import com.ssafy.api.request.GameResultReq;
import com.ssafy.api.response.ExerciseLogRes;
import com.ssafy.api.response.ExerciseStatsRatioRes;
import com.ssafy.db.entity.ExerciseLog;
import com.ssafy.db.entity.GameResult;

import java.util.List;

public interface GameResultService {
    // 게임 결과(전적) 저장
    void saveGameResult(GameResultReq gameResultReq);
    // 전적 조회
    List<GameResult> getUserGameRecords(String userId);

    // 유저 기준 경기 결과 계산 (WIN, LOSE, DRAW)
    GameResult.GameResultType getResult(int userScore, int opponentScore);

    }
