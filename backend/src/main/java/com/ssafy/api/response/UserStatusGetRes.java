package com.ssafy.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class UserStatusGetRes {
    private Short armsStats;
    private Short legsStats;
    private Short chestStats;
    private Short absStats;
    private Short backStats;
    private Short staminaStats;
}