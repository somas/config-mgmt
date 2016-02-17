package com.st.config.server.bean;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    @PrePersist
    void onCreate() {
        this.setCreated(new Timestamp((new Date()).getTime()));
    }

    @PreUpdate
    void onUpdate() {
        this.setUpdated(new Timestamp((new Date()).getTime()));
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
