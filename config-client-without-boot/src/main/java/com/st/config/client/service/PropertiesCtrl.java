package com.st.config.client.service;


import com.st.config.client.bean.Properties;
import feign.Param;
import feign.RequestLine;

public interface PropertiesCtrl {

    @RequestLine("GET /properties/{itemKey}/{fieldKey}")
    Properties properties(@Param("itemKey") String itemKey, @Param("fieldKey") String fieldKey);

}
