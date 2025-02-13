package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel("MakeGameIdReq")
public class MakeGameIdReq {
    private String userId;
    private String date;
}
