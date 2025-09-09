package com.scm.scm20.controller;


import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.scm20.entity.User;
import com.scm.scm20.userRepo.UserRepository;
import com.scm.scm20.userService.UserService;



@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/do-login")
    public String getUserPage(){

        return "user/dashboard";
    }


   @GetMapping("/dashboard")
    public String dashboard() {

    return "user/dashboard";
}


    @GetMapping("/profile")
    public String getUserProfile(Authentication authentication,Model model){

        
        return "user/profile";
    }

    @GetMapping("/setting")
    public String getSetting() {

        return "user/setting";
    }

    
    
}
