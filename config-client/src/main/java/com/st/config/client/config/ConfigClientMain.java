package com.st.config.client.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableMBeanExport
@ComponentScan("com.st.config.client")
public class ConfigClientMain {

    private static final Logger log = LoggerFactory.getLogger(ConfigClientMain.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConfigClientMain.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
