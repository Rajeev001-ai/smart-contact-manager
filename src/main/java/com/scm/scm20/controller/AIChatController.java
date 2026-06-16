package com.scm.scm20.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.scm.scm20.userService.AIChatService;

@RestController
@RequestMapping("/user/api/ai")
public class AIChatController {

    @Autowired
    private AIChatService aiChatService;

    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> body) {
        return aiChatService.processMessage(body.get("message"));
    }
}