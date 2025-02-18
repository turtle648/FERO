package com.ssafy.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankUpdateRes {
    private String user1Id;
    private short user1PreviousScore;
    private short user1NewScore;
//    private String user2Id;
//    private short user2PreviousScore;
//    private short user2NewScore;
}