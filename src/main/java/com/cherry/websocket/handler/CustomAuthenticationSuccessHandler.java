package com.cherry.websocket.handler;


import com.cherry.websocket.dao.UserDao;
import com.cherry.websocket.po.UserPo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserDao userDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserPo userPo = userDao.findByUsername(userDetails.getUsername()).orElse(null);

        HttpSession session = request.getSession();
        session.setAttribute("user", userPo);


        String referer = request.getHeader("Referer");
        // 如果上一次訪問的頁面是登入頁面，則將用戶重定向到首頁
        if (referer != null && referer.contains("/login")) {
            response.sendRedirect("/");
        } else {
            // 否則將用戶重定向到上一次訪問的頁面
            response.sendRedirect(referer);
        }
    }

}