package com.cherry.websocket.dao;


import com.cherry.websocket.po.MessagePo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageDao extends JpaRepository<MessagePo, Long> {

    List<MessagePo> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

}