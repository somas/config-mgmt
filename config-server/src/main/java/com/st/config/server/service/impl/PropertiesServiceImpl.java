package com.st.config.server.service.impl;

import com.st.config.server.bean.AuditLog;
import com.st.config.server.bean.Properties;
import com.st.config.server.dao.PropertiesRepository;
import com.st.config.server.message.RefreshMessageProducer;
import com.st.config.server.service.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class PropertiesServiceImpl implements PropertiesService {
    @Resource
    private PropertiesRepository propertiesRepository;

    @Autowired
    private RefreshMessageProducer refreshMessageProducer;

    public Properties getProperty(String propertiesId) {
        return propertiesRepository.findOne(propertiesId);
    }

    public Properties getPropertyByItemKeyAndFieldKey(String itemKey, String fieldKey) {
        return propertiesRepository.findTopByItemKeyAndFieldKeyOrderByVersionDesc(itemKey, fieldKey);
    }

    public Properties getPropertyByItemKeyAndFieldKeyAndVersion(String itemKey, String fieldKey, int version) {
        return propertiesRepository.findByItemKeyAndFieldKeyAndVersion(itemKey, fieldKey, version);
    }

    public List<Integer> getVersionsByItemKeyAndFieldKey(String itemKey, String fieldKey) {
        return propertiesRepository.findVersionByItemKeyAndFieldKeyOrderByVersionDesc(itemKey, fieldKey);
    }

    public Properties createProperty(Properties property, String principalName) {
        property.setId(java.util.UUID.randomUUID().toString());
        property.setAuditLog(buildAuditLog(property, principalName));
        Properties postCreateProperty = propertiesRepository.save(property);
        refreshMessageProducer.createMessage(property.getItemKey(), property.getFieldKey());
        return postCreateProperty;
    }

    public Properties updateProperty(Properties property, String principalName) {
        Properties prevVersion = getPropertyByItemKeyAndFieldKey(property.getItemKey(), property.getFieldKey());
        property.setVersion(prevVersion.getVersion() + 1);
        property.setId(java.util.UUID.randomUUID().toString());
        property.setAuditLog(buildAuditLog(property, principalName));
        Properties postUpdateProperty = propertiesRepository.save(property);
        refreshMessageProducer.createMessage(property.getItemKey(), property.getFieldKey());
        return postUpdateProperty;
    }

    private AuditLog buildAuditLog(Properties property, String principalName) {
        AuditLog auditLog = new AuditLog();
        auditLog.setId(java.util.UUID.randomUUID().toString());
        auditLog.setUserId(principalName);
        return auditLog;
    }

    public List<String> findItemKeys(String itemKey){
        return propertiesRepository.findItemKeys(itemKey);
    }

    public List<String> findFieldKeysForItemKeys(String itemKey, String fieldKey){
        return propertiesRepository.findFieldKeyForItemKeys(itemKey, fieldKey);
    }
}
