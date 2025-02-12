package com.ssafy.api.service;

import com.ssafy.api.request.GameResultReq;
import com.ssafy.db.entity.GameResult;
import com.ssafy.db.repository.GameResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameResultServiceImpl {

    private final GameResultRepository gameResultRepository;

    // 경기 결과 저장 (두 개의 레코드 생성)
    @Transactional
    public void saveGameResult(GameResultReq request) {

        // 새로운 gameId 생성 (가장 최근 gameId + 1 가져오기)
        Long latestGameId = gameResultRepository.findLatestGameId().orElse(0L);
        Long newGameId = latestGameId + 1;

        GameResult record1 = new GameResult();
        record1.setUserId(request.getUser1Id());
        record1.setGameId(newGameId);
        record1.setUserScore(request.getUser1Score());
        record1.setExerciseId(request.getExerciseId());
        record1.setOpponentId(request.getUser2Id());
        record1.setOpponentScore(request.getUser2Score());
        record1.setResult(getResult(request.getUser1Score(), request.getUser2Score()));
        gameResultRepository.save(record1);

        GameResult record2 = new GameResult();
        record2.setUserId(request.getUser2Id());
        record2.setGameId(newGameId);
        record2.setUserScore(request.getUser2Score());
        record2.setExerciseId(request.getExerciseId());
        record2.setOpponentId(request.getUser1Id());
        record2.setOpponentScore(request.getUser1Score());
        record2.setResult(getResult(request.getUser2Score(), request.getUser1Score()));

        gameResultRepository.save(record2);
    }

    // 유저 기준 경기 결과 계산 (WIN, LOSE, DRAW) -- 프론트에서 승패 정보 받아오면 필요없음,,
    private GameResult.GameResultType getResult(int userScore, int opponentScore) {
        if (userScore > opponentScore) return GameResult.GameResultType.WIN;
        if (userScore < opponentScore) return GameResult.GameResultType.LOSE;
        return GameResult.GameResultType.DRAW;
    }

    // 유저의 최근 경기 전적 조회
    public List<GameResult> getUserGameRecords(String userId) {
        return gameResultRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
