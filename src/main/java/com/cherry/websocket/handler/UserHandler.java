package com.cherry.websocket.handler;

import com.cherry.websocket.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserHandler {

    private static UserDao userDao;

    @Autowired
    public void setMessageDao(UserDao userDao) {
        UserHandler.userDao = userDao;
    }

    public static UserDao getMessageDao() {
        return userDao;
    }
}
