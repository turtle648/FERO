package com.ssafy.api.controller;

import com.ssafy.api.response.GetTutorialRes;
import com.ssafy.api.service.UserTutorialProgressService;
import com.ssafy.api.service.UserTutorialProgressServiceImpl;
import com.ssafy.common.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 유저 튜토리얼 관련 API를 정의하기 위한 컨트롤러 정의.
 */
@Api(value = "튜토리얼 API", tags = {"Tutorial"})
@RestController
@RequestMapping("/api/v1/Tutorial")
public class TutorialController {


    private final UserTutorialProgressService userTutorialProgressService;

    public TutorialController(UserTutorialProgressServiceImpl userTutorialProgressService, UserTutorialProgressService userTutorialProgressService1) {
        this.userTutorialProgressService = userTutorialProgressService1;
    }

    @GetMapping
    @ApiOperation(value = "캐릭터의 튜토리얼 정보 불러오기", notes = "캐릭터의 모든 튜토리얼 현황 조회")
    public ResponseEntity<List<GetTutorialRes>> getTutorials(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String userId = JwtTokenUtil.getUserIdFromJWT(token);  // JWT 토큰에서 userId 추출

        List<GetTutorialRes> tutorials = userTutorialProgressService.getTutorials(userId);

        return ResponseEntity.ok(tutorials);
    }

    @GetMapping("/{tutorialId}")
    @ApiOperation(value = "캐릭터의 특정 튜토리얼 완료 여부 확인", notes = "유저가 특정 튜토리얼을 완료했는지 확인.")
    public ResponseEntity<Boolean> isTutorialComleted(HttpServletRequest request, @PathVariable Long tutorialId) {

        String token = request.getHeader("Authorization");
        String userId = JwtTokenUtil.getUserIdFromJWT(token);  // JWT 토큰에서 userId 추출

        boolean isCompleted = userTutorialProgressService.isTutorialCompleted(userId, tutorialId);

        return ResponseEntity.ok(isCompleted);
    }

    @PostMapping("/complete/{tutorialId}")
    @ApiOperation(value = "튜토리얼 완료 처리", notes = "유저가 특정 튜토리얼을 완료했다고 기록한다.")
    public ResponseEntity<Boolean> completeTutorial(@PathVariable Long tutorialId, HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String userId = JwtTokenUtil.getUserIdFromJWT(token);

        userTutorialProgressService.completeTutorial(userId, tutorialId);

        return ResponseEntity.ok().build();
    }
}
