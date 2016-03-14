package com.st.config.server.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class RefreshMessageConsumer {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    @Qualifier("restTemplateWithoutRibbon")
    private RestTemplate restTemplate;

    @JmsListener(destination = "client-refresh")
    public void processMessage(Map message) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("CONFIG-CLIENT");
        for(ServiceInstance eachInstance: serviceInstances) {

            ResponseEntity<Void> entity = restTemplate.exchange
                    (eachInstance.getUri() + "/client/refresh/{itemKey}/{fieldKey}", HttpMethod.PUT, null, Void.class, message);
            System.out.println(entity.getStatusCode());
        }
        System.out.println("Consumed map message number {} " + message);
    }
}