package com.ssafy.api.response;

import com.ssafy.db.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("VideoRoomResponse")
public class VideoRoomRes {
	@ApiModelProperty(name="토큰")
	String token;

	public static VideoRoomRes of(String token) {
		VideoRoomRes res = new VideoRoomRes();
		res.setToken(token);
		return res;
	}
}
