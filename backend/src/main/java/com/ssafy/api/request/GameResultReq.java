package com.ssafy.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameResultReq {
    private Long exerciseId;
    private String user1Id;
    private String user2Id;
    private short user1Score;
    private short user2Score;
}
