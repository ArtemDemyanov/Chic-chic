package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Портфолио")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @lombok.Setter
    @lombok.Getter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "пользователь_id")
    @JsonBackReference
    User user;

    @lombok.Setter
    @lombok.Getter
    private int experience;

    @lombok.Setter
    @lombok.Getter
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private List<byte[]> certificates;

    @lombok.Setter
    @lombok.Getter
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private List<byte[]> photos;

    @lombok.Setter
    @lombok.Getter
    String description;

    public Portfolio(){
        super();
    }

    public Portfolio(User user, int experience, List<byte[]> certificates, List<byte[]> photos, String description ) {
        this.user = user;
        this.experience = experience;
        this.certificates = certificates;
        this.photos = photos;
        this.description = description;
    }
}
