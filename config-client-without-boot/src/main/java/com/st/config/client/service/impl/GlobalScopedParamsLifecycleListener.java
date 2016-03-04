package com.st.config.client.service.impl;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class GlobalScopedParamsLifecycleListener implements SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(GlobalScopedParamsLifecycleListener.class);

    @Autowired
    private com.st.config.client.service.impl.GlobalScopedParams globalScopedParams;

    @Override
    public void start() {
        logger.info("--- Ready to initialize globalScopedParameters ---");
        ((GlobalScopedParamsImpl)globalScopedParams).initialize();
        ApplicationInfoManager.getInstance().setInstanceStatus(InstanceInfo.InstanceStatus.UP);
    }

    @Override
    public void stop() {
        ApplicationInfoManager.getInstance().setInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
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
