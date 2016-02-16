package com.squaretrade.config.server.service;

import com.squaretrade.config.server.bean.Properties;
import com.squaretrade.config.server.dao.PropertiesRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PropertiesService {
    @Resource
    private PropertiesRepository propertiesRepository;

    public Properties getProperty(String propertiesId) {
        return propertiesRepository.findOne(propertiesId);
    }

    public Properties getPropertyByItemKeyAndFieldKey(String itemKey, String fieldKey) {
        return propertiesRepository.findTopByItemKeyAndFieldKeyOrderByVersionDesc(itemKey, fieldKey);
    }

    public Properties getPropertyByItemKeyAndFieldKeyAndVersion(String itemKey, String fieldKey, int version) {
        return propertiesRepository.findByItemKeyAndFieldKeyAndVersion(itemKey, fieldKey, version);
    }

    public Properties createProperty(Properties property) {
        property.setId(java.util.UUID.randomUUID().toString());
        return propertiesRepository.save(property);
    }

    public Properties updateProperty(Properties property) {
        Properties prevVersion = getPropertyByItemKeyAndFieldKey(property.getItemKey(), property.getFieldKey());
        property.setVersion(prevVersion.getVersion() + 1);
        property.setId(java.util.UUID.randomUUID().toString());
        return propertiesRepository.save(property);
    }
}
