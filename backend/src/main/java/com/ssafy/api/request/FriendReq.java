package com.ssafy.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendReq {
    private String userId;   // 요청을 보낸 유저의 ID
    private String friendId; // 친구가 될 유저의 ID
}