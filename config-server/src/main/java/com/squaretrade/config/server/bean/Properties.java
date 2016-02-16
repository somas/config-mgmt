package com.squaretrade.config.server.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "properties")
public class Properties {

    @Id
    private String id;

    private String projectId;

    private String itemKey;

    private String fieldKey;

    private String description;

    private int version;

    @Column(name="last_updated")
    private Date timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Properties{" +
                "id='" + id + '\'' +
                ", projectId='" + projectId + '\'' +
                ", itemKey='" + itemKey + '\'' +
                ", fieldKey='" + fieldKey + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                ", timestamp=" + timestamp +
                '}';
    }
}
