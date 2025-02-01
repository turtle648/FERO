package com.ssafy.api.controller;

import com.ssafy.api.request.FindUserIdReq;
import com.ssafy.api.request.FindUserPwReq;
import com.ssafy.api.request.UserUpdateReq;
import com.ssafy.api.response.UserInfoRes;
import com.ssafy.api.service.EmailService;
import com.ssafy.api.service.UserCharacterService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

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
	private UserCharacterService userCharacterService;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserCharacterRepository userCharacterRepository;
	@Autowired
	private EmailService emailService;


	private String extractUserIdFromToken(String token) {
		// 'Bearer ' 부분 제거
		String accessToken = token.replace("Bearer ", "");

		// 토큰 검증
		if (!JwtTokenUtil.validateToken(accessToken)) {
			throw new IllegalArgumentException("Invalid Access Token");
		}

		// 사용자 ID 추출
		return JwtTokenUtil.getUserIdFromJWT(accessToken);
	}

	@PostMapping("/login")
	@ApiOperation(value = "로그인", notes = "<strong>아이디와 패스워드</strong>를 통해 로그인 한다.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "성공", response = UserLoginPostRes.class),
        @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
		@ApiResponse(code = 403, message = "임시 비밀번호 사용중", response = BaseResponseBody.class),
        @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
        @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
	public ResponseEntity<UserLoginPostRes> login(@RequestBody @ApiParam(value="로그인 정보", required = true) UserLoginPostReq loginInfo) {
		String userId = loginInfo.getId();
		String password = loginInfo.getPassword();
//		System.out.println("Login ID: " + userId); // Check the userId being passed
//		System.out.println("Password: " + password); // Check the password being passed

		User user = userService.getUserByUserId(userId);

		// 로그인 요청한 유저로부터 입력된 패스워드와 디비에 저장된 유저의 암호화된 패스워드가 같은지 확인.(유효한 패스워드인지 여부 확인)
		if(passwordEncoder.matches(password, user.getUserPassword())) {
			// 임시 비밀번호로 로그인 된 경우 비밀번호 변경 필요하다는 응답.
			if(user.getIsTemporaryPw()) {
				return ResponseEntity.ok(UserLoginPostRes.of(200, "임시 비밀번호로 로그인되었습니다. 비밀번호를 변경해주세요.", JwtTokenUtil.getToken(userId), true));
			}
			// 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
			return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", JwtTokenUtil.getToken(userId), false));
		}
		// 유효하지 않는 패스워드인 경우, 로그인 실패로 응답.
		return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "Invalid Password", null, false));
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

		System.out.println("로그아웃!!");
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

		// 헤더에서 토큰을 통해 사용자 ID 추출
		String userId = extractUserIdFromToken(authHeader);

		// 조회된 사용자 정보
		User userInfo = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
		UserCharacter userCharacter = userCharacterRepository.findByUser_UserId(userId).orElseThrow(() -> new RuntimeException("User character not found"));

		// UserInfoRes로 변환하여 응답 준비
		UserInfoRes userInfoRes = UserInfoRes.of(userInfo, userCharacter);

		return ResponseEntity.ok(userInfoRes);
	}

	@PutMapping("/update")
	@ApiOperation(value = "회원 본인 정보 수정", notes = "로그인한 회원 본인의 정보를 수정한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = UserInfoRes.class),
			@ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
			@ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
	})
	public ResponseEntity<UserInfoRes> updateUserInfo(
			@RequestBody UserUpdateReq updateInfo,
			HttpServletRequest request) {

		// 헤더에서 토큰을 통해 사용자 ID 추출
		String authHeader = request.getHeader("Authorization");
		String userId = extractUserIdFromToken(authHeader);

		// 사용자 이메일, 전화번호 업데이트
		User updatedUser = userService.updateUser(updateInfo, userId);

		// 사용자 캐릭터 닉네임 업데이트
		UserCharacter updatedCharacter = userCharacterService.updateUserCharacter(userId, updateInfo);

		UserInfoRes userInfoRes = getUserInfo(request).getBody();

		// 두 개의 업데이트된 데이터를 반환
		return ResponseEntity.ok(userInfoRes);
	}

	@PostMapping("/check-password")
	@ApiOperation(value = "기존 비밀번호 검증", notes = "사용자의 기존 비밀번호를 검증")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
			@ApiResponse(code = 401, message = "잘못된 비밀번호", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
	})
	public ResponseEntity<BaseResponseBody> checkCurrentPassword(
			@RequestParam String currentPassword, HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		String userId = extractUserIdFromToken(authHeader);

		User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// 기존 비밀번호와 DB에 저장된 비밀번호를 비교
		if (passwordEncoder.matches(currentPassword, user.getUserPassword())) {
			return ResponseEntity.ok(BaseResponseBody.of(200, "기존 비밀번호와 일치"));
		}

		// 잘못된 비밀번호인 경우
		return ResponseEntity.status(401).body(BaseResponseBody.of(401, "일치하지 않음"));
	}

	@PutMapping("/update-password")
	@ApiOperation(value = "새 비밀번호로 업데이트", notes = "새 비밀번호로 사용자의 비밀번호를 업데이트한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
			@ApiResponse(code = 400, message = "잘못된 요청", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
	})
	public ResponseEntity<BaseResponseBody> updatePassword(
			@RequestParam String newPassword, HttpServletRequest request) {

		String authHeader = request.getHeader("Authorization");
		String userId = extractUserIdFromToken(authHeader);

		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// 새 비밀번호 암호화 후 저장
		user.setUserPassword(passwordEncoder.encode(newPassword));
		user.setIsTemporaryPw(false); // 임시 비밀번호 아님
		userRepository.save(user);

		return ResponseEntity.ok(BaseResponseBody.of(200, "비밀번호가 변경되었습니다."));
	}

	@PutMapping("/delete")
	@ApiOperation(value = "회원 탈퇴", notes = "로그인한 사용자의 계정을 비활성화(is_valid=false) 처리한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
			@ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
			@ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
	})
	public ResponseEntity<BaseResponseBody> deleteUser(HttpServletRequest request) {
		// 헤더에서 Authorization 토큰을 가져옴
		String authHeader = request.getHeader("Authorization");
		// 토큰에서 userId 추출
		String userId = extractUserIdFromToken(authHeader);

		// 사용자 조회
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		// is_valid 값을 false로 변경
		user.setIsValid(false);
		userRepository.save(user);
		logout(request);

		return ResponseEntity.ok(BaseResponseBody.of(200, "User successfully deactivated"));
	}

	@PostMapping("/find-id")
	@ApiOperation(value = "회원 ID 찾기", notes = "이름과 이메일을 입력받아 회원의 user_id를 찾고, 이메일로 전송한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
			@ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
	})
	public ResponseEntity<BaseResponseBody> findUserId(@RequestBody FindUserIdReq request) {
		// 1. user_name과 user_email이 일치하는 사용자 찾기
		Optional<User> userOptional = userRepository.findByUserNameAndUserEmail(request.getUserName(), request.getUserEmail());

		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(BaseResponseBody.of(404, "해당하는 회원이 없습니다."));
		}

		// 2. 사용자 존재 시 이메일 전송
		User user = userOptional.get();
		boolean emailSent = emailService.sendUserIdEmail(user.getUserEmail(), user.getUserId());

		if (!emailSent) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(BaseResponseBody.of(500, "이메일 전송 실패"));
		}

		return ResponseEntity.ok(BaseResponseBody.of(200, "이메일로 user_id를 전송했습니다."));
	}

	@PostMapping("/find-password")
	@ApiOperation(value = "비밀번호 찾기", notes = "사용자에게 임시 비밀번호를 이메일로 전송한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
			@ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
	})
	public ResponseEntity<BaseResponseBody> findPassword(@RequestBody FindUserPwReq request){
		// 1. 사용자 찾기 (user_email - user_id)
		Optional<User> userOptional = userRepository.findByUserEmailAndUserId(request.getUserEmail(), request.getUserId());

		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(BaseResponseBody.of(404, "해당하는 회원이 없습니다."));
		}
		User user = userOptional.get();

		// 2. 임시 비밀번호 생성
		String tempPW = generateTempPw();
		user.setUserPassword(passwordEncoder.encode(tempPW));
		user.setIsTemporaryPw(true);

		// 3. 이메일로 임시 비밀번호 전송
		boolean emailSent = emailService.sendUserPwEmail(user.getUserEmail(), tempPW);
		if (!emailSent) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(BaseResponseBody.of(500, "이메일 전송 실패"));
		}

		user.setIsTemporaryPw(true);
		userRepository.save(user);
		return ResponseEntity.ok(BaseResponseBody.of(200, "이메일로 임시 비밀번호를 전송했습니다."));
	}

	// 임시 비밀번호 생성 (숫자, 영문자, 특수문자 포함 9자 이상)
	private String generateTempPw() {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
		Random random = new Random();
		StringBuilder password = new StringBuilder();

		// 최소 9자 이상, 숫자, 영문자, 특수문자 포함
		password.append(characters.charAt(random.nextInt(62))); // 영문 대문자, 소문자, 숫자 중 하나
		password.append(characters.charAt(random.nextInt(62)));
		password.append(characters.charAt(random.nextInt(62)));
		password.append(characters.charAt(random.nextInt(62)));
		password.append(characters.charAt(random.nextInt(62)));
		password.append(characters.charAt(random.nextInt(62)));
		password.append(characters.charAt(random.nextInt(62)));
		password.append(characters.charAt(random.nextInt(62)));
		password.append("!"); // 특수문자

		// 문자열을 문자 배열로 변환하고 섞기
		char[] chars = password.toString().toCharArray();
		for (int i = chars.length - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			char temp = chars[i];
			chars[i] = chars[j];
			chars[j] = temp;
		}

		// 섞인 문자 배열을 다시 문자열로 변환
		return new String(chars);
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
