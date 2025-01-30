package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @lombok.Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name="usluga_id")
    @JsonBackReference
    @lombok.Setter
    @lombok.Getter
    private Usluga usluga;

    @OneToOne(mappedBy = "slot", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.Getter @lombok.Setter
    @JsonIgnore
    private Application application;

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

    public void updateFrom(Slot other) {
        this.time = other.time;
        this.date = other.date;
        this.isAvailable = other.isAvailable;

    }
}
