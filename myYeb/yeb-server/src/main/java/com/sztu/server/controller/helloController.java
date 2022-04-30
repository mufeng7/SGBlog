package com.sztu.server.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloController {

    @GetMapping("/hello")
    public void getHello(){
        System.out.println("hello");
    }
}
