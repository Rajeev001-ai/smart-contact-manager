package com.scm.scm20.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.scm.scm20.Helpers.Message;
import com.scm.scm20.Helpers.messageType;
import com.scm.scm20.entity.User; 
import com.scm.scm20.userRepo.UserRepository;
import com.scm.scm20.userService.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepo;

    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }

    @GetMapping("/contact")
    public String getContact() {
        return "contact";
    }

    @GetMapping("/signup")
    public String getSingup(Model model){

        model.addAttribute("user",new User());

        return "signup";
    }

    @GetMapping("/service")
    public String getService(){

        return "service";
    }

    @PostMapping("/do-register")
    public String formProcess(@Valid @ModelAttribute User user, BindingResult result, HttpSession session){


        if(result.hasErrors()){
            return "signup";
        }

        if(userService.isUserExistByEmail(user.getEmail())){
            Message message=Message.builder().content("Email already registered  !! ").type(messageType.danger).build();
            session.setAttribute("message",message);
            return "redirect:/signup";
        }

        userService.saveUser(user);

        Message message=Message.builder().content("Registration successfully  !! ").type(messageType.success).build();

        session.setAttribute("message",message);

        return "redirect:/signup";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }
    
}


