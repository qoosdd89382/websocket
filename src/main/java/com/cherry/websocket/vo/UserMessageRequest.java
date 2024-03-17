package com.cherry.websocket.vo;

import lombok.Data;

@Data
public class UserMessageRequest {
    private Long receiverId;
    private String messageContent;

    @Override
    public String toString() {
        return "ReceivedMsg{" +
                "receiverId='" + receiverId + '\'' +
                ", messageContent='" + messageContent + '\'' +
                '}';
    }
}
