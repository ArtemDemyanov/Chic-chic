package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "Слот")
public class Slot {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @lombok.Getter
    private Long id;

    @lombok.Setter
    @lombok.Getter
    @ManyToOne
    @JoinColumn(name="услуга_id")
    @JsonBackReference
    private Usluga usluga;

    @lombok.Setter
    @lombok.Getter
    private Date date;

    @lombok.Setter
    @lombok.Getter
    @Column
    private Time time;

    @lombok.Setter
    @lombok.Getter
    @Column
    private boolean isAvailable = true;

    public Slot() {
        super();
    }

    public Slot(Date date, Time time, boolean isAvailable) {
        super();
        this.date = date;
        this.time = time;
        this.isAvailable = isAvailable;
    }
}

