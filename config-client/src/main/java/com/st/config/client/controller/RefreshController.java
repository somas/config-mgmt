package com.st.config.client.controller;


import com.st.config.client.service.GlobalScopedParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class RefreshController {

    @Autowired
    private GlobalScopedParams globalScopedParams;

    @RequestMapping(value = "/refresh/{itemKey}/{fieldKey}", method = RequestMethod.PUT)
    public void refresh(@PathVariable String itemKey, @PathVariable String fieldKey) {
        System.out.println("refreshed");
        globalScopedParams.reinitialize(itemKey, fieldKey);
    }

}
