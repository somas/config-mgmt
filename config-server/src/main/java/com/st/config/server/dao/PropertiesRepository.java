package com.st.config.server.dao;

import com.st.config.server.bean.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertiesRepository extends JpaRepository<Properties, String> {
    Properties findTopByItemKeyAndFieldKeyOrderByVersionDesc(String itemKey, String fieldKey);
    Properties findByItemKeyAndFieldKeyAndVersion(String itemKey, String fieldKey, int version);

    @Query(value = "SELECT p.version FROM Properties p WHERE p.itemKey = :itemKey and p.fieldKey = :fieldKey order by p.version desc")
    List<Integer> findVersionByItemKeyAndFieldKeyOrderByVersionDesc(@Param("itemKey") String itemKey, @Param("fieldKey") String fieldKey);

    @Query(value = "SELECT DISTINCT p.itemKey FROM Properties p WHERE p.itemKey LIKE CONCAT(:itemKey, '%')")
    List<String> findItemKeys(@Param("itemKey") String itemKey);

    @Query(value = "SELECT DISTINCT p.fieldKey FROM Properties p WHERE p.itemKey = :itemKey and p.fieldKey LIKE CONCAT(:fieldKey, '%')")
    List<String> findFieldKeyForItemKeys(@Param("itemKey") String itemKey, @Param("fieldKey") String fieldKey);

}