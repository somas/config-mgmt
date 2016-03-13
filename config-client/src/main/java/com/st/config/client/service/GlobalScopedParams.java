package com.st.config.client.service;

public interface GlobalScopedParams {
    String get(String itemKey, String fieldKey, String key, String defaultVal);

    String get(String itemKey, String fieldKey, String key);

    void reinitialize(String itemKey, String fieldKey);
}
