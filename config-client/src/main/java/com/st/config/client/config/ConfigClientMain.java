package com.st.config.client.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableMBeanExport
@ComponentScan("com.st.config.client")
@EnableDiscoveryClient
public class ConfigClientMain {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConfigClientMain.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
