package com.cherry.websocket.vo;

import com.cherry.websocket.enumeration.ServerMessageRequestType;
import com.cherry.websocket.po.MessagePo;
import lombok.Data;

import java.util.List;

@Data
public class ServerMessageRequest {
    private ServerMessageRequestType type;
    private List<MessagePo> messages;
    private List<UserInfo> users;
}
