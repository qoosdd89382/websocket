package com.cherry.websocket.channel;


import com.cherry.websocket.handler.MessageHandler;
import com.cherry.websocket.handler.UserHandler;
import com.cherry.websocket.po.MessagePo;
import com.cherry.websocket.po.UserPo;
import com.cherry.websocket.vo.UserInfo;
import com.cherry.websocket.vo.UserMessageRequest;
import com.cherry.websocket.vo.ServerMessageRequest;
import com.google.gson.Gson;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.cherry.websocket.enumeration.ServerMessageRequestType.MESSAGES;
import static com.cherry.websocket.enumeration.ServerMessageRequestType.USERS;

@ServerEndpoint(value = "/channel/{userId}")
@Slf4j
public class ChatChannel {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    // 收到消息
    @OnMessage
    public void onMessage(@PathParam("userId") String userId,
                          String messageJsonString,
                          Session session) {
        UserMessageRequest userMessageRequest = new Gson().fromJson(messageJsonString, UserMessageRequest.class);
        Long receiverId = userMessageRequest.getReceiverId();
        Long senderId = Long.valueOf(userId);

        MessagePo savedMessagePo = this.saveMessage(senderId, userMessageRequest);
        ServerMessageRequest serverMessageRequest = new ServerMessageRequest();
        serverMessageRequest.setType(MESSAGES);
        serverMessageRequest.setMessages(List.of(savedMessagePo));
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(new Gson().toJson(serverMessageRequest));
        }
        Session receiverSession = sessions.get(receiverId.toString());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.getAsyncRemote().sendText(new Gson().toJson(serverMessageRequest));
        }
    }

    private MessagePo saveMessage(Long senderId, UserMessageRequest userMessageRequest) {
        MessagePo messagePo = MessagePo.builder()
                .senderId(senderId)
                .receiverId(userMessageRequest.getReceiverId())
                .content(userMessageRequest.getMessageContent())
                .sentAt(new Date())
                .build();
        return MessageHandler.getMessageDao()
                .save(messagePo);
    }

    // 连接打开
    @OnOpen
    public void onOpen(@PathParam("userId") String userId,
                       Session session, EndpointConfig endpointConfig){
        sessions.put(userId, session);

        List<MessagePo> messagePos = this.loadHistoryMessages(
                Long.valueOf(userId));
        ServerMessageRequest serverMessageRequest = new ServerMessageRequest();
        serverMessageRequest.setType(MESSAGES);
        serverMessageRequest.setMessages(messagePos);
        session.getAsyncRemote().sendText(new Gson().toJson(serverMessageRequest));

        sessions.values().forEach(this::sendUsers);
    }

    private void sendUsers(Session session) {
        ServerMessageRequest serverMessageRequestForUsers = new ServerMessageRequest();
        serverMessageRequestForUsers.setType(USERS);
        List<UserInfo> users = sessions.keySet().stream()
                .map(id -> {
                    Optional<UserPo> po = UserHandler.getMessageDao().findById(Long.valueOf(id));
                    return po
                            .map(userPo -> UserInfo.builder()
                                    .id(userPo.getId())
                                    .username(userPo.getUsername())
                                    .build())
                            .orElse(null);
                })
                .filter(Objects::nonNull)
                .toList();
        serverMessageRequestForUsers.setUsers(users);
        session.getAsyncRemote().sendText(new Gson().toJson(serverMessageRequestForUsers));
    }

    private List<MessagePo> loadHistoryMessages(Long userId) {
        return MessageHandler.getMessageDao()
                .findBySenderIdOrReceiverId(userId, userId);
    }

    @OnClose
    public void onClose(@PathParam("userId") String userId,
                        CloseReason closeReason) {
        sessions.remove(userId);
        sessions.values().forEach(this::sendUsers);
    }

    @OnError
    public void onError(@PathParam("userId") String userId,
                        Throwable throwable) throws IOException {
        sessions.remove(userId);
        sessions.values().forEach(this::sendUsers);
    }
}