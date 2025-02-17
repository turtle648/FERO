package com.ssafy.api.controller;

import com.ssafy.api.request.FriendReq;
import com.ssafy.api.response.FriendRes;
import com.ssafy.api.service.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api(value = "친구 관련 API", tags = {"FriendController"})
@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

//    // 친구 요청 보내기
    @PostMapping("/request")
    @ApiOperation(value = "친구 요청", notes = "특정 유저에게 친구 신청")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendReq request) {
        friendService.sendFriendRequest(request.getUserId(), request.getFriendId());
        return ResponseEntity.ok("친구 요청을 보냈습니다.");
    }

//    // 친구 요청 수락
    @PostMapping("/accept")
    @ApiOperation(value = "친구 요청 승인", notes = "특정 유저의 친구 요청을 승인.")
    public ResponseEntity<String> acceptFriendRequest(@RequestBody FriendReq request) {
        friendService.acceptFriendRequest(request.getUserId(), request.getFriendId());
        return ResponseEntity.ok("친구 요청을 수락했습니다.");
    }

//    // 친구 삭제
//    @DeleteMapping("/remove")
//    public ResponseEntity<String> removeFriend(@RequestBody FriendRequest request) {
//        friendService.removeFriend(request.getUserId(), request.getFriendId());
//        return ResponseEntity.ok("친구를 삭제했습니다.");
//    }

    // 친구 목록 조회
    @GetMapping("/{userId}")
    @ApiOperation(value = "친구 목록 조회", notes = "특정 유저의 친구 목록을 조회한다.")
    public ResponseEntity<List<FriendRes>> getFriends(@PathVariable String userId) {
        return ResponseEntity.ok(friendService.getFriends(userId));
    }
}
