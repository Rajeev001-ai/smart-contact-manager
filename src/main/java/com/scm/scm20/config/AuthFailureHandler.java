package com.scm.scm20.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.scm.scm20.Helpers.Message;
import com.scm.scm20.Helpers.messageType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
      

               if(exception instanceof DisabledException){

                HttpSession httpSession=request.getSession();
                Message message=Message.builder().content("User Disabled,Email with verification link is sent on your email id ").type(messageType.danger).build();
                httpSession.setAttribute("message", message);

                response.sendRedirect("/login");
            } 
            else{
                response.sendRedirect("/login?error=true");
            }
    }

}
