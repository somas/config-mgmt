package com.st.config.client.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class GlobalScopedParamsLifecycleListener implements SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(GlobalScopedParamsLifecycleListener.class);

    @Autowired
    private com.st.config.client.service.GlobalScopedParams globalScopedParams;

    @Override
    public void start() {
        logger.info("--- Ready to initialize globalScopedParameters ---");
        ((GlobalScopedParamsImpl)globalScopedParams).initialize();
        logger.info("-------------> " + globalScopedParams.get("demo1", "config-server", "x", "default"));
        logger.info("-------------> " + globalScopedParams.get("demo2", "config-server", "a", "shouldn't work"));
    }

    @Override
    public void stop() {
        logger.info("--- stopping ---");
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(final Runnable callback) {
        logger.info("--- shutdown initiated ---");
    }

    /**
     * This is the most important method.
     * Returning Integer.MAX_VALUE only suggests that
     * we will start after discovery client.
     */
    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
