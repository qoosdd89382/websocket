package com.cherry.websocket.handler;

import com.cherry.websocket.dao.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHandler {

    private static MessageDao messageDao;

    @Autowired
    public void setMessageDao(MessageDao messageDao) {
        MessageHandler.messageDao = messageDao;
    }

    public static MessageDao getMessageDao() {
        return messageDao;
    }
}
