package com.ssafy.api.service;

import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.entity.UserRankScores;
import com.ssafy.db.entity.UserStats;
import com.ssafy.db.repository.UserRankScoresRepository;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserStatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public UserRankScores getRankScoreByUserIdAndType(String userId, String exerciseType) {
        return userRankScoresRepository.findByUser_UserIdAndExerciseType(userId, exerciseType)
                .orElse(null);  // 값이 없으면 null 반환
    }
}
