package com.smohtadi.expenses.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
//@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String slug;
    private LocalDateTime created;
    private LocalDateTime modified;

    public Category(String name, String slug, LocalDateTime created,
                    LocalDateTime modified) {
        this.name = name;
        this.slug = slug;
        this.created = created;
        this.modified = modified;
    }

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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
