package com.scm.scm20.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.scm20.userService.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private OAuthAuthenticationSucessHandler handler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{

        httpSecurity.authorizeHttpRequests(authorize -> {
             
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();

        
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        
         httpSecurity.formLogin(formlogin -> {
    	      formlogin.loginPage("/login");
	          formlogin.loginProcessingUrl("/user/do-login");
   	         //formlogin.successForwardUrl("/user/dashboard");
             // formlogin.failureForwardUrl("/login?error=true");
   	          formlogin.usernameParameter("email");    
   	          formlogin.passwordParameter("password");

              formlogin.failureHandler(authFailureHandler);
    	   });
           
           
             httpSecurity.logout(logoutForm ->{
    	 
    	     logoutForm.logoutUrl("/logout");
	    	 //logoutForm.logoutSuccessUrl("/do-logout");
		     });


             //oauth configuration
             httpSecurity.oauth2Login(oauth -> {

                oauth.loginPage("/login");
                oauth.successHandler(handler);
             });

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        
        return new BCryptPasswordEncoder();
    }
}
