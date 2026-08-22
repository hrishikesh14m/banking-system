package com.banking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/galaxy")
public class GalaxyController {

    @GetMapping
    public String galaxy(){
        return "Welcome to the Galaxy!";
    }

}
