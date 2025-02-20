package com.ssafy.api.service;

import com.ssafy.api.request.GameResultReq;
import com.ssafy.api.request.UserIdGameResultReq;
import com.ssafy.api.response.GameResultsRes;
import com.ssafy.db.entity.GameResult;
import com.ssafy.db.repository.GameResultRepository;
import com.ssafy.db.repository.UserCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameResultServiceImpl implements GameResultService{

    private final GameResultRepository gameResultRepository;
    private final UserCharacterRepository userCharacterRepository;

    // 경기 결과 저장 (두 개의 레코드 생성)
    @EventListener
    @Transactional
    public void saveGameResult(UserIdGameResultReq request) {

        // 새로운 gameId 생성 (가장 최근 gameId + 1 가져오기)

        GameResult record1 = new GameResult();
        record1.setUserId(request.getUser1Id());
        record1.setGameId(request.getGameId());
        record1.setUserScore(request.getUser1Score().shortValue());
        record1.setExerciseId(request.getExerciseId());
        record1.setOpponentId(request.getUser2Id());
        record1.setOpponentScore(request.getUser2Score().shortValue());
        record1.setResult(getResult(request.getUser1Score(), request.getUser2Score()));
        record1.setDuration(request.getDuration());
        gameResultRepository.save(record1);

        GameResult record2 = new GameResult();
        record2.setUserId(request.getUser2Id());
        record2.setGameId(request.getGameId());
        record2.setUserScore(request.getUser2Score().shortValue());
        record2.setExerciseId(request.getExerciseId());
        record2.setOpponentId(request.getUser1Id());
        record2.setOpponentScore(request.getUser1Score().shortValue());
        record2.setResult(getResult(request.getUser2Score(), request.getUser1Score()));
        record2.setDuration(request.getDuration());
        gameResultRepository.save(record2);
    }

    // 유저 기준 경기 결과 계산 (WIN, LOSE, DRAW) -- 프론트에서 승패 정보 받아오면 필요없음,,
    public GameResult.GameResultType getResult(int userScore, int opponentScore) {
        if (userScore > opponentScore) return GameResult.GameResultType.WIN;
        if (userScore < opponentScore) return GameResult.GameResultType.LOSE;
        return GameResult.GameResultType.DRAW;
    }


    // 유저의 최근 경기 전적 조회
    public List<GameResultsRes> getUserGameRecords(String userId) {
        List<GameResult> results = gameResultRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<GameResultsRes> response = results.stream().map((result) ->
            new GameResultsRes(
                    userCharacterRepository.findByUser_UserId(result.getUserId()).get().getUserNickname(),
                    result.getGameId(),
                    result.getDuration(),
                    result.getExerciseId(),
                    userCharacterRepository.findByUser_UserId(result.getOpponentId()).get().getUserNickname(),
                    result.getUserScore(),
                    result.getOpponentScore(),
                    result.getResult(),
                    result.getCreatedAt()
            )
        ).collect(Collectors.toList());
        return response;
    }
}
