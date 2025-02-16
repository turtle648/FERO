package com.ssafy.api.response;

import com.ssafy.db.entity.Friend;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendRes {
    private String friendId;       // 친구의 user_id
    private String friendNickname; // 친구의 닉네임
    private int friendLevel;       // 친구의 레벨
    private Friend.FriendStatus status;


    // 모든 필드를 받는 생성자
    public FriendRes(String friendId, String friendNickname, int friendLevel, Friend.FriendStatus status) {
        this.friendId = friendId;
        this.friendNickname = friendNickname;
        this.friendLevel = friendLevel;
        this.status = status;
    }
}
