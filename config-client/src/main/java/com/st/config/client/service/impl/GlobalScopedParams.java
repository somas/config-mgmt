package com.st.config.client.service.impl;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

/**
 * Created by ssivaraj on 2/22/16.
 */
public interface GlobalScopedParams {
    @ManagedOperation(description="get property")
    @ManagedOperationParameters({
            @ManagedOperationParameter(
                    name="itemKey",
                    description="itemKey"),
            @ManagedOperationParameter(
                    name="fieldKey",
                    description="fieldKey"),
            @ManagedOperationParameter(
                    name="key",
                    description="key"),
            @ManagedOperationParameter(
                    name="defaultValue",
                    description="defaultValue")})
    String get(String itemKey, String fieldKey, String key, String defaultVal);

    String get(String itemKey, String fieldKey, String key);
}
