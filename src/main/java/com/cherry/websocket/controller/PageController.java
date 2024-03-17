package com.cherry.websocket.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/protected")
    public String protectedPage() {
        return "protected";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "login";
    }

//    @PostMapping("/chat")
//    public String chat(@RequestParam String userId,
//                       Model model) {
//        model.addAttribute("userId", userId);
//        return "chat";
//    }


    
}
