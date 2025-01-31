package com.ssafy.api.service;

import com.ssafy.api.request.UserCharacterRegisterPostReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.common.exception.handler.RegistrationException;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.UUID;

@Service
@Slf4j
public class RegistrationService {
    private final UserService userService;
    private final UserCharacterService userCharacterService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RegistrationService(UserService userService, UserCharacterService userCharacterService, 
                             RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.userCharacterService = userCharacterService;
        this.redisTemplate = redisTemplate;
    }

    public String initializeRegistration(UserRegisterPostReq userInfo) {
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("reg:" + sessionId, userInfo, Duration.ofMinutes(30));
        log.info("Started registration process for user: {}", userInfo.getUserId());
        return sessionId;
    }

    @Transactional
    public void completeRegistration(String sessionId, UserCharacterRegisterPostReq characterInfo) {
        UserRegisterPostReq userInfo = (UserRegisterPostReq) redisTemplate.opsForValue().get("reg:" + sessionId);

        if (userInfo == null) {
            throw new RegistrationException("세션이 만료되었습니다. 처음부터 다시 시도해주세요.");
        }

        try {
            // 유저 생성
            User user = userService.createUser(userInfo);

            // 캐릭터 생성 및 연결
            UserCharacter character = new UserCharacter();
            character.setUser(user);
            character.setUserNickname(characterInfo.getUserNickname());
            character.setGender(characterInfo.getGender());
            character.setPushupRecord((short) 0);
            character.setSquatRecord((short) 0);
            character.setPullupRecord((short) 0);
            character.setPoints((short) 0);

            user.setUserCharacter(character); // 양방향 관계 설정
            redisTemplate.delete("reg:" + sessionId);

            log.info("Completed registration for user: {}", user.getUserId());
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            throw new RegistrationException("회원가입 처리 중 오류가 발생했습니다.");
        }
    }
}