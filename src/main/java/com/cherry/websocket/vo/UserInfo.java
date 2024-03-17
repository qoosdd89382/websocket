package com.cherry.websocket.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {

    private Long id;
    private String username;

}
