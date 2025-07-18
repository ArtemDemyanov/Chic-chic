package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "slots")
@Getter
@Setter // Generates getters and setters for all fields
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usluga_id", nullable = false) // Ensure the foreign key is not null
    private Usluga usluga;

    @OneToOne(mappedBy = "slot", cascade = CascadeType.ALL, orphanRemoval = true)
    private Application application;

    @Column(nullable = false) // Ensure the date cannot be null
    private Date date;

    @Column(nullable = false) // Ensure the time cannot be null
    private Time time;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE") // Default value set in the database
    private Boolean isAvailable = true;

    public Slot() {
        super();
    }

    /**
     * Helper method to associate this slot with a Usluga.
     */
    public void setUsluga(Usluga usluga) {
        this.usluga = usluga;
        if (usluga != null && !usluga.getSlots().contains(this)) {
            usluga.getSlots().add(this); // Maintain bidirectional relationship
        }
    }
}