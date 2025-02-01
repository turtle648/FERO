package com.ssafy.api.service;

import com.ssafy.api.request.UserCharacterRegisterPostReq;
import com.ssafy.api.request.UserUpdateReq;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.repository.UserCharacterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service("userCharacterService")
public class UserCharacterServiceImpl implements UserCharacterService {

    private final UserCharacterRepository userCharacterRepository;

    @Autowired
    public UserCharacterServiceImpl(UserCharacterRepository userCharacterRepository) {
        this.userCharacterRepository = userCharacterRepository;
    }

    @Override
    public UserCharacter createUserCharacter(UserCharacterRegisterPostReq userCharacterRegisterInfo, User user) {
        // 이미 존재하는 캐릭터인지 확인
        Optional<UserCharacter> existingCharacter = userCharacterRepository.findByUser_UserId(user.getUserId());

        if (existingCharacter.isPresent()) {
            log.warn("UserCharacter already exists for userId: {}", user.getUserId());
            return existingCharacter.get(); // 기존 캐릭터 반환
        }

        // 새 UserCharacter 생성
        UserCharacter userCharacter = new UserCharacter();
        userCharacter.setUser(user);
        userCharacter.setUserNickname(userCharacterRegisterInfo.getUserNickname());
        userCharacter.setGender(userCharacterRegisterInfo.getGender());

        userCharacter.setPushupRecord((short) 0);
        userCharacter.setSquatRecord((short) 0);
        userCharacter.setPullupRecord((short) 0);
        userCharacter.setPoints((short) 0);

        return userCharacterRepository.save(userCharacter);
    }

    @Override
    public UserCharacter updateUserCharacter(String userId, UserUpdateReq updateInfo) {
        UserCharacter userCharacter = userCharacterRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("UserCharacter not found for userId: " + userId));

        // userNickname 수정
        if (updateInfo.getUserNickname() != null && !updateInfo.getUserNickname().isEmpty()) {
            userCharacter.setUserNickname(updateInfo.getUserNickname());
        }

        userCharacterRepository.save(userCharacter);

        return userCharacter;
    }
}
