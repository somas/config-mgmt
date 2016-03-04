package com.st.config.client.service.impl;

public interface GlobalScopedParams {
    String get(String itemKey, String fieldKey, String key, String defaultVal);

    String get(String itemKey, String fieldKey, String key);
}
