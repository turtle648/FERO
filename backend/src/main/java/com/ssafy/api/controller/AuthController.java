package com.ssafy.api.controller;

import com.ssafy.api.response.UserInfoRes;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.repository.UserCharacterRepository;
import com.ssafy.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ssafy.api.request.UserLoginPostReq;
import com.ssafy.api.response.UserLoginPostRes;
import com.ssafy.api.service.UserService;
import com.ssafy.common.model.response.BaseResponseBody;
import com.ssafy.common.util.JwtTokenUtil;
import com.ssafy.db.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 인증 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "인증 API", tags = {"Auth."})
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserCharacterRepository userCharacterRepository;

	@PostMapping("/login")
	@ApiOperation(value = "로그인", notes = "<strong>아이디와 패스워드</strong>를 통해 로그인 한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공", response = UserLoginPostRes.class),
        @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
        @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
        @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
	public ResponseEntity<UserLoginPostRes> login(@RequestBody @ApiParam(value="로그인 정보", required = true) UserLoginPostReq loginInfo) {
		String userId = loginInfo.getId();
		String password = loginInfo.getPassword();
		System.out.println("Login ID: " + userId); // Check the userId being passed
		System.out.println("Password: " + password); // Check the password being passed

		User user = userService.getUserByUserId(userId);

		// 로그인 요청한 유저로부터 입력된 패스워드와 디비에 저장된 유저의 암호화된 패스워드가 같은지 확인.(유효한 패스워드인지 여부 확인)
		if(passwordEncoder.matches(password, user.getUserPassword())) {
			// 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
			return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", JwtTokenUtil.getToken(userId)));
		}
		// 유효하지 않는 패스워드인 경우, 로그인 실패로 응답.
		return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "Invalid Password", null));
	}

	@PostMapping("/logout")
	@ApiOperation(value = "로그아웃", notes = "로그아웃을 수행한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
			@ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
	})
	public ResponseEntity<BaseResponseBody> logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(BaseResponseBody.of(401, "Unauthorized"));
		}

		String accessToken = authHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");

		// 토큰 검증
		if (!JwtTokenUtil.validateToken(accessToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponseBody.of(401, "Invalid Access Token"));
		}

		String userId = JwtTokenUtil.getUserIdFromJWT(accessToken);

		return ResponseEntity.ok(BaseResponseBody.of(200, "Logout Successful"));
	}

	@GetMapping("/me")
	@ApiOperation(value = "회원 본인 정보 조회", notes = "로그인한 회원 본인의 정보를 응답한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = UserInfoRes.class),
			@ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
			@ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
	})
	public ResponseEntity<UserInfoRes> getUserInfo(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		System.out.println("여기 1 : "+authHeader);
		if (authHeader == null || authHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserInfoRes.of(null, null));
		}


		String accessToken = authHeader.replace(JwtTokenUtil.TOKEN_PREFIX, "");
		System.out.println("여기 2 : "+accessToken);

		// 토큰 검증
		if (!JwtTokenUtil.validateToken(accessToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(UserInfoRes.of(null, null));
		}

		String userId = JwtTokenUtil.getUserIdFromJWT(accessToken);
		System.out.println("1 사용자 ID ? : "+ userId);

		// 조회된 사용자 정보
		User userInfo = userRepository.findByUserId(userId).get();
		System.out.println("2 사용자 정보 ? : "+userInfo);
		UserCharacter userCharacter = userCharacterRepository.findByUser_UserId(userId).get();
		System.out.println("3 사용자 정보 ? : "+userCharacter);

		// UserInfoRes로 변환하여 응답 준비
		UserInfoRes userInfoRes = UserInfoRes.of(userInfo, userCharacter);

		System.out.println("4 사용자 정보 ? : "+userInfoRes);

		return ResponseEntity.ok(userInfoRes);
	}


//	public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
//		String token = request.getHeader("Authorization").replace("Bearer ", "");
//
//		// JWT의 만료 시간 가져오기
//		Date expiration = JwtTokenUtil.getVerifier().verify(token).getExpiresAt();
//		long expirationTime = expiration.getTime() - System.currentTimeMillis();
//		System.out.println("JWT 만료시간 : "+ expirationTime);
//
//		// Redis에 토큰 저장 (만료 시간만큼 유효하게 설정)
//		redisTemplate.opsForValue().set(token, "logout", expirationTime, TimeUnit.MILLISECONDS);
//
//		Map<String, Object> response = new HashMap<>();
//		response.put("message", "Success");
//		response.put("statusCode", 200);
//		return ResponseEntity.ok(response);
//	}

}
