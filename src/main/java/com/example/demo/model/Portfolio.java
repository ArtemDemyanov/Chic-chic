package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Portfolio")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    private int experience;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] certificate;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photos;

    String description;
    public Portfolio(){

    }
    public Portfolio(User user, byte[] photos, String description ) {
        this.user = user;
        this.photos = photos;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getPhotos() {
        return photos;
    }

    public void setPhotos(byte[] photos) {
        this.photos = photos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
