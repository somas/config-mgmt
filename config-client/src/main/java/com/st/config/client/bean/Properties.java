package com.st.config.client.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Properties {

    private String id;

    private String itemKey;

    private String fieldKey;

    private String description;

    private int version;

    private Timestamp created;

}
