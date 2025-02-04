package com.ssafy.api.service;

import com.ssafy.api.request.UserCharacterRegisterPostReq;
import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.common.exception.handler.RegistrationException;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.repository.UserCharacterRepository;
import com.ssafy.db.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final UserCharacterRepository usercharacterRepository;

    @Autowired
    public RegistrationService(UserService userService, UserCharacterService userCharacterService, 
                             RedisTemplate<String, Object> redisTemplate,
                               EmailService emailService,
                               UserRepository userRepository, UserCharacterRepository usercharacterRepository) {
        this.userService = userService;
        this.userCharacterService = userCharacterService;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.usercharacterRepository = usercharacterRepository;
    }

    /**
     * 2. 이메일 인증 요청 (코드 발송)
     */
    public void sendVerificationEmail(String userEmail) {
        String verificationCode = UUID.randomUUID().toString().substring(0, 6);
        redisTemplate.opsForValue().set("email_verification:"+userEmail,
                verificationCode, Duration.ofMinutes(4));

        boolean emailSent = emailService.sendVerificationEmail(userEmail, verificationCode);
        if (!emailSent) throw new RuntimeException("이메일 전송 실패");
    }

    /**
     * 3. 이메일 인증 확인
     */
    public boolean verifyEmail(String useremail, String inputCode) {
        String storedCode = (String) redisTemplate.opsForValue().get("email_verification:"+useremail);
        redisTemplate.delete("email_verification:" + useremail); // 항상 삭제해야 함!
        if (storedCode == null || !storedCode.equals(inputCode)) {
            redisTemplate.opsForValue().set("verified:" + useremail, true, Duration.ofMinutes(4));
            redisTemplate.delete("email_verification:"+useremail); // 인증 성공하면 삭제함
            return true;
        }
        return false;
    }

    /**
     * 4. 회원 기본 정보 입력 (이메일 인증 확인)
     */
    public String initializeRegistration(UserRegisterPostReq userInfo) {
        // 중복 체크
        if (userRepository.existsByUserId(userInfo.getUserId())) {
            throw new RegistrationException("이미 존재하는 아이디입니다.");
        }
        if (userRepository.existsByUserEmail(userInfo.getUserEmail())) {
            throw new RegistrationException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.existsByPhoneNumber(userInfo.getPhoneNumber())) {
            throw new RegistrationException("이미 존재하는 전화번호입니다.");
        }

        String sessionId = UUID.randomUUID().toString();

//        // 이메일 인증 코드 생성
//        String verificationCode = UUID.randomUUID().toString().substring(0, 6); // 6자리 랜덤 코드

        // Redis에 저장 (이메일 인증 상태 포함)
        redisTemplate.opsForValue().set("reg:" + sessionId, userInfo, Duration.ofMinutes(30));
//        redisTemplate.opsForValue().set("email:" + userInfo.getUserEmail(), verificationCode, Duration.ofMinutes(10));
//        redisTemplate.opsForValue().set("verified:" + sessionId, false, Duration.ofMinutes(30));

//        // 이메일 전송 (이메일 인증 코드)
//        emailService.sendVerificationEmail(userInfo.getUserEmail(), verificationCode);

        log.info("Started registration process for user: {}", userInfo.getUserId());
        return sessionId;
    }
    /**
     * 5. 회원 캐릭터 정보 입력 및 회원가입 완료
     */
    @Transactional
    public void completeRegistration(String sessionId, UserCharacterRegisterPostReq characterInfo) {
        // 세션 체크
        UserRegisterPostReq userInfo = (UserRegisterPostReq) redisTemplate.opsForValue().get("reg:" + sessionId);
        if (userInfo == null) {
            throw new RegistrationException("세션이 만료되었습니다. 처음부터 다시 시도해주세요.");
        }

        // 중복 체크
        if (userRepository.existsByUserId(userInfo.getUserId())) {
            throw new RegistrationException("이미 존재하는 아이디입니다.");
        }
        if (usercharacterRepository.existsByUserNickname(characterInfo.getUserNickname())) {
            throw new RegistrationException("이미 존재하는 닉네임입니다.");
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
            character.setPoints((short) 0);

            user.setUserCharacter(character); // 양방향 관계 설정
            usercharacterRepository.save(character);

            redisTemplate.delete("reg:" + sessionId);
            log.info("Completed registration for user: {}", user.getUserId());

        } catch (Exception e) {
            log.error("Registration failed", e);
            throw new RegistrationException("회원가입 처리 중 오류가 발생했습니다."+e.getMessage());
        }
    }
}