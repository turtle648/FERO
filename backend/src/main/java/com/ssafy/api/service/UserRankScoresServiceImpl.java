package com.ssafy.api.service;

import com.ssafy.api.request.ExerciseResultEvent;
import com.ssafy.api.response.RankUpdateRes;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.UserRankScores;
import com.ssafy.db.repository.UserRankScoresRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("UserRankScoreService")
public class UserRankScoresServiceImpl implements UserRankScoresService {


    private final UserRankScoresRepository userRankScoresRepository;

    public UserRankScoresServiceImpl(UserRankScoresRepository userRankScoresRepository) {
        this.userRankScoresRepository = userRankScoresRepository;
    }

    @Override
    public List<UserRankScores> getAllRankScoresByUserId(String userId) {
        return userRankScoresRepository.findByUser_UserId(userId);
    }


    @Override
    public UserRankScores getRankScoreByUserIdAndId(String userId, Long exerciseId) {
        return userRankScoresRepository.findByUser_UserIdAndId(userId, exerciseId)
                .orElseThrow(null);  // 값이 없으면 null 반환
    }

    @Override
    @Transactional
    public RankUpdateRes updateRankScore(ExerciseResultEvent event) {
        log.info("========================updateRankScore===========================");
        String user1Id = event.getUser1Id();
        String user2Id = event.getUser2Id();
        Long exerciseId = event.getExerciseType();
        double result = event.getResult();

        log.info(">>> eventListener : {}", event);

        // 유저1 & 유저2의 특정 운동 랭크 점수 조회
        UserRankScores user1Rank = userRankScoresRepository
                .findByUser_UserIdAndExerciseStatsRatio_Id(user1Id, exerciseId)
                .orElseThrow(() -> new RuntimeException("유저1의 랭크 데이터 없음"));

        UserRankScores user2Rank = userRankScoresRepository
                .findByUser_UserIdAndExerciseStatsRatio_Id(user2Id, exerciseId)
                .orElseThrow(() -> new RuntimeException("유저2의 랭크 데이터 없음"));

        log.info(">>> user1Rank : {}", user1Rank.getUser());
        log.info(">>> user2Rank : {}", user2Rank.getUser());

        // 현재 점수 가져오기
        short user1PrevScore = user1Rank.getRankScore();
        short user2PrevScore = user2Rank.getRankScore();

        log.info(">>> user1PrevScore : {}", user1PrevScore);

        // 결과에 따른 Elo 점수 계산
        short user1ChangeScore, user2ChangeScore;
        if (result == 1) { // user1 승리
            user1ChangeScore = calculateEloChange(user1PrevScore, user2PrevScore, true);
//            user2ChangeScore = calculateEloChange(user2PrevScore, user1PrevScore, false);
        } else if (result == 2) { // user2 승리
            user1ChangeScore = calculateEloChange(user1PrevScore, user2PrevScore, false);
//            user2ChangeScore = calculateEloChange(user2PrevScore, user1PrevScore, true);
        } else { // "DRAW"
            user1ChangeScore = calculateEloChange(user1PrevScore, user2PrevScore, null);
//            user2ChangeScore = calculateEloChange(user2PrevScore, user1PrevScore, null);
        }

        // 새로운 점수 계산
        short user1NewScore = (short) (user1PrevScore + user1ChangeScore);
//        short user2NewScore = (short) (user2PrevScore + user2ChangeScore);

        log.info(">>> user1NewScore : {}", user1NewScore);

        // DB 반영
        userRankScoresRepository.updateUserRankScore(user1Id, exerciseId, user1ChangeScore);
//        userRankScoresRepository.updateUserRankScore(user2Id, exerciseId, user2ChangeScore);

        log.info("Elo 점수 업데이트 완료! [운동 ID: {}] 유저1: {} ({} → {})",
                exerciseId, user1Id, user1PrevScore, user1NewScore);
//                user2Id, user2PrevScore, user2NewScore);

        return new RankUpdateRes(
                user1Id, user1PrevScore, user1NewScore);
//                user2Id, user2PrevScore, user2NewScore
//        );
    }

    private short calculateEloChange(short playerScore, short opponentScore, Boolean isWinner) {
        int kFactor = 32;
        double expectedScore = 1 / (1 + Math.pow(10, (opponentScore - playerScore) / 400.0));

        double actualScore;
        if (isWinner == null) { // 무승부 처리
            log.info("무승부!");
            actualScore = 0.5;
        } else if (isWinner) {
            actualScore = 1.0;
        } else {
            actualScore = 0.0;
        }

        return (short) (kFactor * (actualScore - expectedScore));
    }

}
