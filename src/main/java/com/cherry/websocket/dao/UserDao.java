package com.cherry.websocket.dao;


import com.cherry.websocket.po.UserPo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<UserPo, Long> {

    Optional<UserPo> findByUsername(String username);

}