package com.ssafy.api.service;

import com.ssafy.api.request.RankUpdateReq;
import com.ssafy.api.response.RankUpdateRes;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.entity.UserRankScores;
import com.ssafy.db.entity.UserStats;
import com.ssafy.db.repository.UserRankScoresRepository;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserStatsRepository;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service("userRankScoreService")
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
    public RankUpdateRes updateRankScore(String winnerId, String loserId, Long exerciseId) {
        // 승자 & 패자의 특정 운동 랭크 점수 조회
        UserRankScores winnerRank = userRankScoresRepository
                .findByUser_UserIdAndExerciseStatsRatio_Id(winnerId, exerciseId)
                .orElseThrow(() -> new RuntimeException("승자의 랭크 데이터 없음"));

        UserRankScores loserRank = userRankScoresRepository
                .findByUser_UserIdAndExerciseStatsRatio_Id(loserId, exerciseId)
                .orElseThrow(() -> new RuntimeException("패자의 랭크 데이터 없음"));

        // 현재 점수 가져오기
        short winnerPrevScore = winnerRank.getRankScore();
        short loserPrevScore = loserRank.getRankScore();
        short changeScore = (short) calculateEloChange(winnerPrevScore, loserPrevScore, true);

        // 새로운 점수 계산
        short winnerNewScore = (short) (winnerPrevScore + changeScore);
        short loserNewScore = (short) (loserPrevScore - changeScore);

        // DB 반영
        userRankScoresRepository.updateUserRankScore(winnerId, exerciseId, changeScore);
        userRankScoresRepository.updateUserRankScore(loserId, exerciseId, changeScore);

        log.info("Elo 점수 업데이트 완료! [운동 ID: {}] 승자: {} ({} → {}), 패자: {} ({} → {})",
                exerciseId, winnerId, winnerRank.getRankScore(), winnerRank.getRankScore() + changeScore,
                loserId, loserRank.getRankScore(), loserRank.getRankScore() + changeScore);

        return new RankUpdateRes(
                winnerId, winnerPrevScore, winnerNewScore,
                loserId, loserPrevScore, loserNewScore
        );
    }

    private short calculateEloChange(short playerScore, short opponentScore, boolean isWinner) {
        int kFactor = 32;
        double expectedScore = 1 / (1 + Math.pow(10, (opponentScore - playerScore) / 400.0));
        short scoreChange = (short) (kFactor * ((isWinner ? 1 : 0) - expectedScore));
        return scoreChange;
    }

}
