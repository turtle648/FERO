package com.ssafy.api.response;

import com.ssafy.common.model.response.BaseResponseBody;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 유저 로그인 API ([POST] /api/v1/auth) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@ApiModel("UserLoginPostResponse")
public class UserLoginPostRes extends BaseResponseBody{
	@ApiModelProperty(name="JWT 인증 토큰", example="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN...")
	String accessToken;

	@ApiModelProperty(name = "임시 비밀번호 여부", example = "false")
	boolean isTempPw;
	
	public static UserLoginPostRes of(Integer statusCode, String message, String accessToken, boolean isTempPw) {
		UserLoginPostRes res = new UserLoginPostRes();
		res.setStatusCode(statusCode);
		res.setMessage(message);
		res.setAccessToken(accessToken);
		res.setTempPw(isTempPw);
		return res;
	}
}
