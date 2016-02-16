package com.squaretrade.config.server.dao;

import com.squaretrade.config.server.bean.Properties;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertiesRepository extends JpaRepository<Properties, String> {
    Properties findTopByItemKeyAndFieldKeyOrderByVersionDesc(String itemKey, String fieldKey);
    Properties findByItemKeyAndFieldKeyAndVersion(String itemKey, String fieldKey, int version);
}
