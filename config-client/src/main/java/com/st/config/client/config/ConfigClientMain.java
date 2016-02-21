package com.st.config.client.config;

import com.st.config.client.service.impl.GlobalScopedParamsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("com.st.config.client")
public class ConfigClientMain implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ConfigClientMain.class);

    @Autowired
    private GlobalScopedParamsImpl globalScopedParams;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ConfigClientMain.class);
    }

    public void run(String... strings) throws Exception {
        globalScopedParams.buildProperty();
        log.info(globalScopedParams.getProperty("amazon", "integration", "x"));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
