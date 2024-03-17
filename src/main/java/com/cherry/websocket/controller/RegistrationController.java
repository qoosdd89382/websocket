package com.cherry.websocket.controller;


import com.cherry.websocket.dao.UserDao;
import com.cherry.websocket.po.UserPo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(UserPo userPo, Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }

        if (userDao.findByUsername(userPo.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        String encodedPassword = passwordEncoder.encode(userPo.getPassword());
        userPo.setPassword(encodedPassword);

        // 儲存新用戶到資料庫
        userDao.save(userPo);

        return "redirect:/login"; // 重定向到登入頁面
    }
}