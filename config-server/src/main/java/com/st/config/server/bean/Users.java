package com.st.config.server.bean;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class Users {

    @Id
    private String id;
    private String username;
    private String password;
    private String role;

    @Column(insertable=true, updatable=false)
    private Timestamp created;

    @Column(insertable=false, updatable=true)
    private Timestamp updated;

    @PrePersist
    void onCreate() {
        this.setCreated(new Timestamp((new Date()).getTime()));
    }

    @PreUpdate
    void onUpdate() {
        this.setUpdated(new Timestamp((new Date()).getTime()));
    }
}
