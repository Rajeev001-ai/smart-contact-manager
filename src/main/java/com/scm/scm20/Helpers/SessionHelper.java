package com.scm.scm20.Helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHelper {


    //for remove message from the signup form
    public static void removeMessage(){

        try{
            HttpSession session=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            
            session.removeAttribute("message");
        }
        catch(Exception e){
            System.out.println("Error in sessionhelper "+e);
            e.printStackTrace();
        }
    }
}
