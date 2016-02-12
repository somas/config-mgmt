package com.squaretrade.config.server.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.squaretrade.config.server")
//@EnableJpaRepositories("com.squaretrade.config.server.dao")
//@EntityScan("com.squaretrade.config.server.bean")
public class ConfigServerMain {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConfigServerMain.class, args);
    }
}
