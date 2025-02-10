package com.ssafy.api.controller;

import com.ssafy.api.request.UserCharacterRegisterPostReq;
import com.ssafy.api.response.UserInfoRes;
import com.ssafy.common.exception.handler.RegistrationException;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import com.ssafy.api.response.UserStatusGetRes;
import com.ssafy.api.service.RegistrationService;
import com.ssafy.api.service.UserCharacterService;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ssafy.api.request.UserRegisterPostReq;
import com.ssafy.api.service.UserService;
import com.ssafy.common.model.response.BaseResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Optional;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

	@Autowired
    UserCharacterService userCharacterService;

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 1. 이메일 중복 확인
     */
    @GetMapping("/check-email")
    @ApiOperation(value = "1. 이메일 중복 확인", notes = "사용자가 입력한 이메일이 중복인지 확인")
    public ResponseEntity<BaseResponseBody> checkEmailDuplication(@RequestParam String email) {
        boolean exists = userService.existsByUserEmail(email);

        if (exists) {
            return ResponseEntity.status(409).body(BaseResponseBody.of(409, "이미 사용 중인 이메일입니다."));
        } else {
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "사용 가능한 이메일입니다."));
        }
    }

    /**
     * 2. 이메일 인증 요청 (코드 발송)
     */
    @PostMapping("/send-email")
    @ApiOperation(value = "2. 이메일 인증 코드 발송", notes = "사용자의 이메일로 인증 코드를 보냅니다.")
    public ResponseEntity<BaseResponseBody> sendVerificationEmail(@RequestParam String email) {
        registrationService.sendVerificationEmail(email);
        return ResponseEntity.ok(BaseResponseBody.of(200, "이메일 인증 코드가 발송되었습니다."));
    }

    /**
     * 3. 이메일 인증 확인
     */
    @PostMapping("/verify-email")
    @ApiOperation(value = "3. 이메일 인증 코드 확인하기", notes = "이메일 인증 코드 확인")
    public ResponseEntity<BaseResponseBody> verifyEmail(@RequestParam String email, @RequestParam String code) {
        boolean verified = registrationService.verifyEmail(email, code);
        if (!verified) {
            return ResponseEntity.status(400).body(BaseResponseBody.of(400, "인증 코드가 유효하지 않습니다."));
        }
        return ResponseEntity.ok(BaseResponseBody.of(200, "이메일 인증 성공"));
    }

    /**
     * 4. 회원 기본 정보 입력
     */
    @PostMapping()
    @ApiOperation(value = "4. 회원 기본 정보 입력", notes = "이메일 인증이 완료된 후 회원 기본 정보 입력 반영")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원가입 초기화 성공 (세션 ID 반환)"),
            @ApiResponse(code = 400, message = "잘못된 요청 (필수 정보 누락)"),
            @ApiResponse(code = 409, message = "중복된 데이터 존재 (아이디/이메일/전화번호)"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends BaseResponseBody> saveUserInfo(
            @RequestBody @ApiParam(value = "회원가입 정보", required = true) UserRegisterPostReq registerInfo) {

        try {
            // 이메일 인증 여부 확인
            Boolean isVerified = (Boolean) redisTemplate.opsForValue().get("verified:" + registerInfo.getUserEmail());
            if (isVerified == null || !isVerified) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponseBody.of(401, "이메일 인증을 먼저 완료해주세요."));
            }

            String sessionId = registrationService.initializeRegistration(registerInfo);
            return ResponseEntity.ok(BaseResponseBody.of(200, sessionId));
        } catch (RegistrationException e) {
            if (e.getMessage().contains("이미 존재하는")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(BaseResponseBody.of(409, e.getMessage()));
            }
            return ResponseEntity.badRequest()
                    .body(BaseResponseBody.of(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponseBody.of(500, "서버 오류: " + e.getMessage()));
        }
    }
    /**
     * 5. 캐릭터 정보 입력
     */
    @PostMapping("/character")
    @ApiOperation(value = "5. 캐릭터 정보입력", notes = "닉네임을 지을 수 있고, 캐릭터의 성별을 결정할 수 있다.")
    public ResponseEntity<? extends BaseResponseBody> CharacterRegister(
            @RequestParam @ApiParam(value = "세션 ID", required = true) String sessionId,
            @RequestBody @ApiParam(value = "캐릭터 정보", required = true) UserCharacterRegisterPostReq characterRegisterinfo) {

        try {
            registrationService.completeRegistration(sessionId, characterRegisterinfo);
            return ResponseEntity.ok(BaseResponseBody.of(200, "회원가입이 완료되었습니다."));
        } catch (RegistrationException e) {
            if (e.getMessage().contains("이미 존재하는")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(BaseResponseBody.of(409, e.getMessage()));
            } else if (e.getMessage().contains("세션이 만료") ||
                    e.getMessage().contains("이메일 인증")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(BaseResponseBody.of(401, e.getMessage()));
            }
            return ResponseEntity.badRequest()
                    .body(BaseResponseBody.of(400, e.getMessage()));
        }
    }
}
