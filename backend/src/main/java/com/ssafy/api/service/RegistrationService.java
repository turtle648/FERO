package com.ssafy.api.service;

import com.ssafy.api.request.UserCharacterRegisterPostReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.common.exception.handler.RegistrationException;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.repository.UserCharacterRepository;
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
    private final EmailService emailService;

    @Autowired
    public RegistrationService(UserService userService, UserCharacterService userCharacterService, 
                             RedisTemplate<String, Object> redisTemplate,
                               EmailService emailService) {
        this.userService = userService;
        this.userCharacterService = userCharacterService;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
    }

    // 이메일 인증 요청 (인증 코드 생성 후 이메일 전송)
    public void sendVerificationEmail(String userEmail) {
        String verificationCode = UUID.randomUUID().toString().substring(0, 6);
        redisTemplate.opsForValue().set("email_verification:"+userEmail,
                verificationCode, Duration.ofMinutes(4));

        boolean emailSent = emailService.sendVerificationEmail(userEmail, verificationCode);
        if (!emailSent) throw new RuntimeException("이메일 전송 실패");
    }

    // 이메일 인증 확인 (사용자가 입력한 코드 검증)
    public boolean verifyEmail(String useremail, String inputCode) {
        String storedCode = (String) redisTemplate.opsForValue().get("email_verification:"+useremail);
        redisTemplate.delete("email_verification:" + useremail); // 항상 삭제해야 함!
        if (storedCode == null || !storedCode.equals(inputCode)) {
            redisTemplate.delete("email_verification:"+useremail); // 인증 성공하면 삭제함
            return true;
        }
        return false;
    }

    @Autowired
    UserCharacterRepository userCharacterRepository;

    public String initializeRegistration(UserRegisterPostReq userInfo) {
        String sessionId = UUID.randomUUID().toString();

        // 이메일 인증 코드 생성
        String verificationCode = UUID.randomUUID().toString().substring(0, 6); // 6자리 랜덤 코드

        // Redis에 저장 (이메일 인증 상태 포함)
        redisTemplate.opsForValue().set("reg:" + sessionId, userInfo, Duration.ofMinutes(30));
        redisTemplate.opsForValue().set("email:" + userInfo.getUserEmail(), verificationCode, Duration.ofMinutes(10));
        redisTemplate.opsForValue().set("verified:" + sessionId, false, Duration.ofMinutes(30));

        // 이메일 전송 (이메일 인증 코드)
        emailService.sendVerificationEmail(userInfo.getUserEmail(), verificationCode);

        log.info("Started registration process for user: {}", userInfo.getUserId());
        return sessionId;
    }

    @Transactional
    public void completeRegistration(String sessionId, UserCharacterRegisterPostReq characterInfo) {
        UserRegisterPostReq userInfo = (UserRegisterPostReq) redisTemplate.opsForValue().get("reg:" + sessionId);

        if (userInfo == null) {
            throw new RegistrationException("세션이 만료되었습니다. 처음부터 다시 시도해주세요.");
        }

        // 이메일 인증 여부 확인
        Boolean isVerified = (Boolean) redisTemplate.opsForValue().get("verified:" + userInfo.getUserEmail());
        if (isVerified == null || !isVerified) {
            throw new RegistrationException("이메일 인증이 완료되지 않았습니다.");
        }

        try {
            // 유저 생성
            User user = userService.createUser(userInfo);

            // 캐릭터 생성 및 연결
            UserCharacter character = new UserCharacter();
            character.setUser(user);
            character.setId(user.getId());
            character.setUserNickname(characterInfo.getUserNickname());
            character.setGender(characterInfo.getGender());
//            character.setPushupRecord((short) 0);
//            character.setSquatRecord((short) 0);
//            character.setPullupRecord((short) 0);
            character.setPoints((short) 0);

            user.setUserCharacter(character); // 양방향 관계 설정

            userCharacterRepository.save(character);

            redisTemplate.delete("reg:" + sessionId);

            log.info("Completed registration for user: {}", user.getUserId());
        } catch (Exception e) {
            log.error("Registration failed", e);
            throw new RegistrationException("회원가입 처리 중 오류가 발생했습니다.");
        }
    }
}