package com.st.config.server.service;

import com.st.config.server.bean.Properties;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface PropertiesService {

    Properties getProperty(String propertiesId);

    Properties getPropertyByItemKeyAndFieldKey(String itemKey, String fieldKey);

    Properties getPropertyByItemKeyAndFieldKeyAndVersion(String itemKey, String fieldKey, int version);

    List<Integer> getVersionsByItemKeyAndFieldKey(String itemKey, String fieldKey);

    Properties createProperty(Properties property, String principalName);

    @PreAuthorize("hasAuthority('ADMIN')")
    Properties updateProperty(Properties property, String principalName);

    List<String> findItemKeys(String itemKey);

    List<String> findFieldKeysForItemKeys(String itemKey, String fieldKey);
}
