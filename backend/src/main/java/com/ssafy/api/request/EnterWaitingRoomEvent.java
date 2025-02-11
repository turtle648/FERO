package com.ssafy.api.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EnterWaitingRoomEvent {
    private final String userToken;
    private final Long exerciseId;
    private final Short rankScore;
}
