package com.st.config.client.config;

import com.st.config.client.bean.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConfigClientMain implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ConfigClientMain.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConfigClientMain.class);
    }

    public void run(String... strings) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Properties> properties = restTemplate.exchange
                ("http://localhost:9080/properties/1", HttpMethod.GET, new HttpEntity<>(createHeaders()), Properties.class);
        log.info(properties.toString());
    }

    HttpHeaders createHeaders( ){
        return new HttpHeaders(){
            {
                String authHeader = "Basic " + new String( "c29tYXM6cGFzc3dvcmQ=" );
                set( "Authorization", authHeader );
            }
        };
    }
}
