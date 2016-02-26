package com.st.service.discovery.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ConfigDiscoveryServer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConfigDiscoveryServer.class);
    }

}


//https://github.com/kbastani/spring-cloud-microservice-example