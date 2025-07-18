package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Услуги")
@Getter
@Setter // This generates getters and setters for all fields
public class Usluga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String category;

    @ManyToOne
    @JoinColumn(name = "пользователь_id")
    private User user;

    private String location;
    private String coordinates;

    @OneToMany(mappedBy = "usluga", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Slot> slots = new ArrayList<>();

    private double price;
    private int durationMinutes;

    @lombok.Setter
    @lombok.Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ModerationStatus status = ModerationStatus.PENDING;

    public Usluga() {
        super();
    }

    public Usluga(String name, String description, String category, User user, String location, String coordinates, double price, int durationMinutes) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.user = user;
        this.location = location;
        this.coordinates = coordinates;
        this.price = price;
        this.durationMinutes = durationMinutes;
    }
}