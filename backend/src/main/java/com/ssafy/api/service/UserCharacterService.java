package com.ssafy.api.service;

import com.ssafy.api.request.UserCharacterRegisterPostReq;
import com.ssafy.api.request.UserUpdateReq;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;

/**
 *	유저 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserCharacterService {
	UserCharacter createUserCharacter(UserCharacterRegisterPostReq userCharacterRegisterInfo, User user);
	UserCharacter updateUserCharacter(String userId, UserUpdateReq updateInfo);
	UserCharacter getUserCharacterByUserId(String userId);
}
