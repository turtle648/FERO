package com.ssafy.api.controller;

import com.ssafy.api.request.UserCharacterRegisterPostReq;
import com.ssafy.api.response.UserInfoRes;
import com.ssafy.api.service.*;
import com.ssafy.common.exception.handler.RegistrationException;
import com.ssafy.db.entity.UserStats;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import com.ssafy.api.response.UserStatusGetRes;
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
 * 유저 스탯, 레벨, 랭크 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 스탯 API", tags = {"UserStats"})
@RestController
@RequestMapping("/api/v1/userStats")
public class UserStatsController {


    private final UserCharacterServiceImpl userCharacterService;
    private final UserStatsServiceImpl userStatsService;

    public UserStatsController(UserCharacterServiceImpl userCharacterService, UserStatsServiceImpl userStatsService) {
        this.userCharacterService = userCharacterService;
        this.userStatsService = userStatsService;
    }

    @GetMapping("/status")
    @ApiOperation(value = "캐릭터 스테이터스 불러오기", notes = "캐릭터의 스테이터스 및 레벨, 랭크를 확인할 수 있다.")
    public ResponseEntity<UserStatusGetRes> getCharacterStatus(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String userId = JwtTokenUtil.getUserIdFromJWT(token);  // JWT 토큰에서 userId 추출

        UserStats userStats = userStatsService.getUserStats(userId);

        UserStatusGetRes response = UserStatusGetRes.builder()
                .armsStats(userStats.getArmsStats())
                .legsStats(userStats.getLegsStats())
                .chestStats(userStats.getChestStats())
                .absStats(userStats.getAbsStats())
                .backStats(userStats.getBackStats())
                .staminaStats(userStats.getStaminaStats())

                .build();

        return ResponseEntity.ok(response);
    }


}
