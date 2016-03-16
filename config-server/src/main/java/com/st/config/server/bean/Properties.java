package com.st.config.server.bean;

import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "properties")
public class Properties {

    @Id
    private String id;

    private String itemKey;

    private String fieldKey;

    private String description;

    private int version;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="audit_id", referencedColumnName="id")
    private AuditLog auditLog;

    @Column(insertable=true, updatable=false)
    private Timestamp created;

    @PrePersist
    void onCreate() {
        this.setCreated(new Timestamp((new Date()).getTime()));
    }
}
