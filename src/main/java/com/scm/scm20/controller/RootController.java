package com.scm.scm20.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.scm20.Helpers.Helper;
import com.scm.scm20.entity.User;
import com.scm.scm20.userService.UserService;

@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedinUserinformation(Model model, Authentication authentication){

        if(authentication  ==  null){
            return;
        }
        
       String username= Helper.getEmailOfLoggedinUser(authentication); 

       User user=userService.getUserByEmail(username);
       
       model.addAttribute("user", user);
    }


}
