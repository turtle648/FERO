package com.ssafy.api.service;

import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.entity.UserStats;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserStatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("userStatsService")
public class UserStatsServiceImpl implements UserStatsService {


    private final UserStatsRepository userStatsRepository;
    private final UserRepository userRepository;

    public UserStatsServiceImpl(UserStatsRepository userStatsRepository, UserRepository userRepository) {
        this.userStatsRepository = userStatsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserStats getUserStats(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));

        return userStatsRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("해당 캐릭터 정보를 찾을 수 없습니다."));
    }

    @Override
    public UserCharacter getUserLevel(String userId) {
        return null;
    }

    @Override
    public UserCharacter getUserRankScore(String userId) {
        return null;
    }
}
