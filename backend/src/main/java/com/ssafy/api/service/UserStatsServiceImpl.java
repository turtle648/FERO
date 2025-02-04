package com.ssafy.api.service;

import com.ssafy.db.entity.UserCharacter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("userStatsService")
public class UserStatsServiceImpl implements UserStatsService {


    @Override
    public UserCharacter getCharacterStats(String userId) {

        return null;
    }

    @Override
    public UserCharacter getCharacterLevel(String userId) {

        return null;
    }

    @Override
    public UserCharacter getCharacterRankScore(String userId) {

        return null;
    }
}
