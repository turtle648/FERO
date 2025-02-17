package com.ssafy.api.response;

import com.ssafy.api.request.WaitingUser;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WaitingRoomStatusRes {
    private String exerciseType;
    private int waitingCount;
    private List<WaitingUser> waitingUsers;
}
