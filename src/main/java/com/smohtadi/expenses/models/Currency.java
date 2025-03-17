package com.smohtadi.expenses.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
//@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String slug;
    @Column(length = 15)
    private String abbr;
    private LocalDateTime created;
    private LocalDateTime modified;

    public Currency(String name, String slug, String abbr,
                    LocalDateTime created, LocalDateTime modified) {
        this.name = name;
        this.slug = slug;
        this.abbr = abbr;
        this.created = created;
        this.modified = modified;
    }

    public Currency() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
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
