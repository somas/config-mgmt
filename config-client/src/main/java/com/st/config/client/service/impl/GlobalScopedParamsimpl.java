package com.st.config.client.service.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.st.config.client.bean.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GlobalScopedParamsImpl {

    @Autowired
    private RestTemplate restTemplate;

    private ConcurrentHashMap<String, Map<String, Object>> propertiesMap = new ConcurrentHashMap<>();
    private static final String KEY_SEPARATOR = "_||_";

    public void buildProperty() throws IOException {
        ResponseEntity<Properties> entity = restTemplate.exchange
                ("http://localhost:9080/properties/1", HttpMethod.GET, new HttpEntity<>(createHeaders()), Properties.class);
        Properties properties = entity.getBody();
        Map<String, Object> propMap = getPropertiesMapFromText(properties.getDescription());
        addOrUpdatePropertiesMap(properties.getItemKey(), properties.getFieldKey(), propMap);
    }

    public String getProperty(String ik, String fk, String key) {
        Map<String, Object> mapOfProperties =  propertiesMap.get(buildKeyFromIKFK(ik, fk));
        return (mapOfProperties != null) ? (String) mapOfProperties.get(key) : null;
    }

    HttpHeaders createHeaders( ){
        return new HttpHeaders(){
            {
                String authHeader = "Basic " + new String( "c29tYXM6cGFzc3dvcmQ=" );
                set( "Authorization", authHeader );
            }
        };
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

}
