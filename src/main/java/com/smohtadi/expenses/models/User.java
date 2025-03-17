package com.smohtadi.expenses.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
//@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String uid;
    private LocalDateTime created;
    private LocalDateTime modified;

    public User(
            String username,
            String uid,
            LocalDateTime created,
            LocalDateTime modified
    ) {
        this.username = username;
        this.uid = uid;
        this.created = created;
        this.modified = modified;
    }

    public User() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }
}
