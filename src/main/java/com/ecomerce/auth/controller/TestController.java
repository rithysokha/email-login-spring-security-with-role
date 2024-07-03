package com.ecomerce.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/admin")
    public String admin(){
        return "hi admin";
    }

    @GetMapping("/user")
    public String user(){
        return "hi user";
    }
}
