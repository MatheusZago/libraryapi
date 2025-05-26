package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //because we are using web pages
public class LoginViewController {

    @GetMapping("/login")
    public String loginPage(){
        return "login"; //Returning with page to direct, same name as WebConfiguration
    }

    @GetMapping("/")
    @ResponseBody
    public String homePage(Authentication authentication){

        if(authentication instanceof CustomAuthentication customAuth){
            System.out.println(customAuth.getUser());
        }

        return "Hello " + authentication.getName();
    }

    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthorizationCode(@RequestParam("code") String code){
        return "Your authorization code is " + code;
    }

}
