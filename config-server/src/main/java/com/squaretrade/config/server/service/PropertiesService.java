package com.squaretrade.config.server.service;

import com.squaretrade.config.server.bean.Properties;
import com.squaretrade.config.server.dao.PropertiesRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PropertiesService {
    @Resource
    private PropertiesRepository propertiesRepository;

    public Properties getUser(String propertiesId) {
        return propertiesRepository.findOne(propertiesId);
    }
}
