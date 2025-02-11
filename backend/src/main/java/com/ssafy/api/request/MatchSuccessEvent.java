package com.ssafy.api.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MatchSuccessEvent {
    private final String userToken1;
    private final String userToken2;
    private final Long exerciseId;
}
