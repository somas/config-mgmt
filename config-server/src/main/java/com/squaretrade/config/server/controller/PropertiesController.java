package com.squaretrade.config.server.controller;


import com.squaretrade.config.server.service.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertiesController {
    @Autowired
    private PropertiesService propertiesService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!" + propertiesService.getUser("1").getLogin();
    }
}
