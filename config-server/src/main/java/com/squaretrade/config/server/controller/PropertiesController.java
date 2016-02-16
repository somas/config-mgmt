package com.squaretrade.config.server.controller;


import com.squaretrade.config.server.bean.Properties;
import com.squaretrade.config.server.service.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/properties")
public class PropertiesController {
    @Autowired
    private PropertiesService propertiesService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Properties getProperty(@PathVariable String id) {
        return propertiesService.getProperty(id);
    }

    @RequestMapping(value = "/{itemKey}/{fieldKey}", method = RequestMethod.GET)
    public Properties getPropertyByItemKeyAndFieldKey(@PathVariable String itemKey, @PathVariable String fieldKey) {
        return propertiesService.getPropertyByItemKeyAndFieldKey(itemKey, fieldKey);
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
