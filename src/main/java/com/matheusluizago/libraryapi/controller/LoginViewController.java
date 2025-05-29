package com.matheusluizago.libraryapi.controller;

import com.matheusluizago.libraryapi.security.CustomAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //because we are using web pages
@Tag(name = "Views")
public class LoginViewController {

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    @Operation(summary = "Homepage", description = "Returns data for the homepage")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page returned successfully.")
    })
    public String homePage(Authentication authentication){

        if(authentication instanceof CustomAuthentication customAuth){
            System.out.println(customAuth.getUser());
        }

        return "Hello " + authentication.getName();
    }

    @GetMapping("/authorized")
    @ResponseBody
    @Operation(summary = "Authorization page", description = "Returns data for the authorization page")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page returned successfully.")
    })
    public String getAuthorizationCode(@RequestParam("code") String code){
        return "Your authorization code is " + code;
    }

}
