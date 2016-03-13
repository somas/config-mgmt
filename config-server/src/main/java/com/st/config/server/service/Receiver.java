package com.st.config.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Receiver {

    @Autowired
    private ConfigurableApplicationContext context;

    @JmsListener(destination = "client-refresh")
    public void processMessage(Map message) {
        System.out.println("Consumed map message number {} " + message);
    }
}