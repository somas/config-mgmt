package com.st.config.client.service.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st.config.client.bean.Properties;
import com.st.config.client.service.PropertiesCtrl;
import com.st.config.client.service.impl.GlobalScopedParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@ManagedResource(
        objectName="GlobalScopedParam:type=bean",
        description="GlobalScoped Parameters" )
public class GlobalScopedParamsImpl implements GlobalScopedParams {
    private static final Logger logger = LoggerFactory.getLogger(GlobalScopedParamsImpl.class);
    private ConcurrentHashMap<String, Map<String, Object>> propertiesMap = new ConcurrentHashMap<>();
    private static final String KEY_SEPARATOR = "_||_";
    private static final String ENV_SPECIFIC_IK = "ENV";
    private static final String DEFAULT_FK = "DEFAULT";
    private static final String GLOBAL_IK = "GLOBAL";

    private String environment = (System.getProperty(ENV_SPECIFIC_IK) == null) ? "DEV" : System.getProperty(ENV_SPECIFIC_IK);

    @Autowired
    private PropertiesCtrl propertiesCtrl;

    @Value("${config.server.url}")
    private String url;

    @Value("${config.server.auth}")
    private String auth;

    /**
     * This is for base properties stored in
     * GLOBAL/DEFAULT, ENVIRONMENT/DEV, ../INT, ../QA, ../STAGE, ../PROD
     */
    void initialize() {
        environment = (System.getProperty(ENV_SPECIFIC_IK) == null) ? "DEV" : System.getProperty(ENV_SPECIFIC_IK);
        initializeProperties(GLOBAL_IK, DEFAULT_FK);
        initializeProperties(ENV_SPECIFIC_IK, environment);
    }

    private void initializeProperties(String itemKey, String fieldKey) {
        Properties properties = propertiesCtrl.properties(itemKey, fieldKey);
        if(properties == null) {
            return; // property doesn't exist
        }
        Map<String, Object> map = null;
        try {
            map = getPropertiesMapFromText(properties.getDescription());
            map.put("x_date_created", properties.getCreated());
            map.put("x_version", properties.getVersion());
        } catch (IOException e) {
            logger.error("Error while reading property: ItemKey : " + itemKey + " , FieldKey : " + fieldKey, e);
            throw new RuntimeException(e); // technically we should never get here unless there was a database corruption
        }
        addOrUpdatePropertiesMap(itemKey, fieldKey, map);
    }

    public Map<String, Object> getPropertiesMapFromText(String jsonText)
            throws IOException {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };

        Map<String, Object> mapOfProperties = mapper.readValue(jsonText, typeRef);
        return mapOfProperties;
    }

    private void addOrUpdatePropertiesMap(String itemKey, String fieldKey, Map<String, Object> map) {
        String key = buildKeyFromIKFK(itemKey, fieldKey);
        propertiesMap.put(key, map);
    }

    private String buildKeyFromIKFK(String itemKey, String fieldKey) {
        return itemKey + KEY_SEPARATOR + fieldKey;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getMapOfString(String itemKey, String fieldKey, String key) {
        if (propertiesMap.get(buildKeyFromIKFK(itemKey, fieldKey)) == null) {
            initializeProperties(itemKey, fieldKey);
        }
        Map<String, Object> tempMap = propertiesMap.get(buildKeyFromIKFK(itemKey, fieldKey));
        return (tempMap != null) ? (Map<String, String>) tempMap.get(key) : null;
    }

    public Map<String, String> getMap(String itemKey, String fieldKey, String key) {
        return getMapOfString(itemKey, fieldKey, key);
    }

    @Override
    @ManagedOperation(description = "get property")
    @ManagedOperationParameters({
            @ManagedOperationParameter(
                    name = "itemKey",
                    description = "itemKey"),
            @ManagedOperationParameter(
                    name = "fieldKey",
                    description = "fieldKey"),
            @ManagedOperationParameter(
                    name = "key",
                    description = "key"),
            @ManagedOperationParameter(
                    name = "defaultValue",
                    description = "defaultValue")})
    public String get(String itemKey, String fieldKey, String key, String defaultVal) {
        if (propertiesMap.get(buildKeyFromIKFK(itemKey, fieldKey)) == null) {
            initializeProperties(itemKey, fieldKey);
        }
        Map<String, Object> tempMap = propertiesMap.get(buildKeyFromIKFK(itemKey, fieldKey));
        return (tempMap != null && tempMap.get(key) != null) ? (String) tempMap.get(key) : defaultVal;
    }

    @Override
    public String get(String itemKey, String fieldKey, String key) {
        return this.get(itemKey, fieldKey, key, null);
    }
}
