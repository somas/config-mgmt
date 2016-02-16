package com.squaretrade.config.server.controller;


import com.squaretrade.config.server.bean.Properties;
import com.squaretrade.config.server.service.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/properties")
public class PropertiesController {
    @Autowired
    private PropertiesService propertiesService;

    @RequestMapping(method= RequestMethod.GET)
    public Properties getProperty() {
        return propertiesService.getProperty("1");
    }

    @RequestMapping(method= RequestMethod.POST)
    public Properties createProperty(@RequestBody Properties properties) {
        return propertiesService.createProperty(properties);
    }

    @RequestMapping(method= RequestMethod.PUT)
    public Properties updateProperty(@RequestBody Properties properties) {
        return propertiesService.updateProperty(properties);
    }
}
