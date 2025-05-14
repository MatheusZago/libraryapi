package com.matheusluizago.libraryapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //because we are using web pages
public class LoginViewController {

    @GetMapping("/login")
    public String loginPage(){
        return "login"; //Returning with page to direct, same name as WebConfiguration
    }

}
