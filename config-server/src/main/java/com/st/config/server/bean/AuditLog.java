package com.st.config.server.bean;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    private String id;

    private String userId;

    @Column(insertable=true, updatable=false)
    private Timestamp created;

    @PrePersist
    void onCreate() {
        this.setCreated(new Timestamp((new Date()).getTime()));
    }
}
