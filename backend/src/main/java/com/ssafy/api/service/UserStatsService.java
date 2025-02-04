package com.ssafy.api.service;

import com.ssafy.db.entity.UserCharacter;

/**
 *	캐릭터 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserStatsService {
	UserCharacter getCharacterStats(String userId);
	UserCharacter getCharacterLevel(String userId);
	UserCharacter getCharacterRankScore(String userId);
}
