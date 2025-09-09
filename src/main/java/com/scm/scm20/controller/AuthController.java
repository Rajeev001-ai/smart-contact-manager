package com.scm.scm20.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.scm20.Helpers.Message;
import com.scm.scm20.Helpers.messageType;
import com.scm.scm20.entity.User;
import com.scm.scm20.userRepo.UserRepository;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession httpSession) {

        User user = userRepository.findByEmailToken(token).orElse(null);

        if (user != null) {

            if (user.getEmailToken().equals(token)) {

                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepository.save(user);

                Message message = Message.builder().content("Your email is verified , Now you can login !! ")
                        .type(messageType.success).build();
                httpSession.setAttribute("message", message);

                return "success-page";
            } else {

                Message message = Message.builder().content("Email not verified ! Token is not associated with user")
                        .type(messageType.success).build();
                httpSession.setAttribute("message", message);

                return "error-page";
            }

        }
        return "error-page";
    }
}
