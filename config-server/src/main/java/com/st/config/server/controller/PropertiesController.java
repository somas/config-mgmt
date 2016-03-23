package com.st.config.server.controller;


import com.st.config.server.bean.Properties;
import com.st.config.server.service.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping(value = "/{itemKey}/{fieldKey}/{version}", method = RequestMethod.GET)
    public Properties getPropertyByItemKeyAndFieldKey(@PathVariable String itemKey, @PathVariable String fieldKey, @PathVariable int version) {
        return propertiesService.getPropertyByItemKeyAndFieldKeyAndVersion(itemKey, fieldKey, version);
    }

    @RequestMapping(value = "/{itemKey}/{fieldKey}/versions", method = RequestMethod.GET)
    public Map<String, List<Integer>> getVersionsByItemKeyAndFieldKey(@PathVariable String itemKey, @PathVariable String fieldKey) {
        List<Integer> versionColl = propertiesService.getVersionsByItemKeyAndFieldKey(itemKey, fieldKey);
        Map<String, List<Integer>> versionMap = new HashMap<>();
        versionMap.put("versions", versionColl);
        return versionMap;
    }

    @RequestMapping(method= RequestMethod.POST)
    public Properties createProperty(@RequestBody Properties properties, Principal principal) {
        return propertiesService.createProperty(properties, principal.getName());
    }

    @RequestMapping(method= RequestMethod.PUT)
    public Properties updateProperty(@RequestBody Properties properties, Principal principal) {
        return propertiesService.updateProperty(properties, principal.getName());
    }

    @RequestMapping(value = "/search/{itemKey}",method= RequestMethod.GET)
    public List<String> getIk(@PathVariable String itemKey) {
        return propertiesService.findItemKeys(itemKey);
    }

    @RequestMapping(value = "/search/{itemKey}/{fieldKey}",method= RequestMethod.GET)
    public List<String> getFk(@PathVariable String itemKey,@PathVariable String fieldKey) {
        return propertiesService.findFieldKeysForItemKeys(itemKey, fieldKey);
    }

}
