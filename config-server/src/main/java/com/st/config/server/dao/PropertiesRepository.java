package com.st.config.server.dao;

import com.st.config.server.bean.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertiesRepository extends JpaRepository<Properties, String> {
    Properties findTopByItemKeyAndFieldKeyOrderByVersionDesc(String itemKey, String fieldKey);
    Properties findByItemKeyAndFieldKeyAndVersion(String itemKey, String fieldKey, int version);

    @Query(value = "SELECT p.version FROM Properties p WHERE p.itemKey = :itemKey and p.fieldKey = :fieldKey")
    List<Integer> findVersionsByItemKeyAndFieldKey(@Param("itemKey") String itemKey, @Param("fieldKey") String fieldKey);
}
