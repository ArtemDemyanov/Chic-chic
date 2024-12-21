package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Услуги")
public class Usluga {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @lombok.Getter
    private Long id;

    @lombok.Setter
    @lombok.Getter
    private String name;

    @lombok.Setter
    @lombok.Getter
    private String description;

    @lombok.Setter
    @lombok.Getter
    private String category;

    @lombok.Setter
    @lombok.Getter
    @ManyToOne
    @JoinColumn(name="пользователь_id")
    @JsonBackReference
    private User user;

    @lombok.Setter
    @lombok.Getter
    private String location;

    @lombok.Setter
    @lombok.Getter
    private String coordinates;

    @lombok.Setter
    @lombok.Getter
    @OneToMany(mappedBy="usluga", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Slot> slots = new ArrayList<>();

    @lombok.Setter
    @lombok.Getter
    private double price;

    @lombok.Setter
    @lombok.Getter
    private int durationMinutes;

    public Usluga() {
        super();
    }

    public Usluga(String name, String description, String category, User user, String location, String coordinates, double price, int durationMinutes) {
        super();
        this.name = name;
        this.description = description;
        this.category = category;
        this.user = user;
        this.location = location;
        this.coordinates = coordinates;
        this.price=price;
        this.durationMinutes=durationMinutes;
    }
}

