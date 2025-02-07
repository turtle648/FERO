package com.ssafy.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchingReq {
    private String userId;
    private String exerciseType;
    private int rankScore;
}
