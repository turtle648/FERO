package com.ssafy.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class UserStatusGetRes {
    private Short armsStatus;
    private Short legsStatus;
    private Short chestStatus;
    private Short absStatus;
    private Short backStatus;
    private Short staminaStatus;

    private String userNickname;
    private Short userLevel;
    private String userRank;
}