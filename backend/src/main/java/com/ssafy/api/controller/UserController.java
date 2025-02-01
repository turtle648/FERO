package com.ssafy.api.controller;

import com.ssafy.api.request.UserCharacterRegisterPostReq;
import com.ssafy.api.response.UserInfoRes;
import com.ssafy.api.service.RegistrationService;
import com.ssafy.api.service.UserCharacterService;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @PostMapping()
    @ApiOperation(value = "회원 가입", notes = "회원가입")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends BaseResponseBody> saveUserInfo(
            @RequestBody @ApiParam(value = "회원가입 정보", required = true) UserRegisterPostReq registerInfo) {

        String sessionId = registrationService.initializeRegistration(registerInfo);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, sessionId));
    }

    @PostMapping("/character")
    @ApiOperation(value = "캐릭터 정보입력", notes = "닉네임을 지을 수 있고, 캐릭터의 성별을 결정할 수 있다.")
    public ResponseEntity<? extends BaseResponseBody> CharacterRegister(
            @RequestParam @ApiParam(value = "세션 ID", required = true) String sessionId,
            @RequestBody @ApiParam(value = "캐릭터 정보", required = true) UserCharacterRegisterPostReq characterRegisterinfo) {

        registrationService.completeRegistration(sessionId, characterRegisterinfo);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Join Success"));
    }
}
