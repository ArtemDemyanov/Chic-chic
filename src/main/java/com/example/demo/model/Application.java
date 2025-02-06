package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "applications")
@Getter
@Setter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usluga_id", nullable = false)
    private Usluga usluga;

    private String applicantName;

    private String uslugaName;

    private String applicantEmail;

    @OneToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    private User master;

    public Application() {
        super();
    }

    public Application(String applicantName, String uslugaName, String applicantEmail, Usluga usluga, User user, User master) {
        this.usluga = usluga;
        this.applicantName = applicantName;
        this.uslugaName = uslugaName;
        this.applicantEmail = applicantEmail;
        this.user = user;
        this.master = master;
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getApplications().remove(this);
        }
        this.user = user;
        if (user != null) {
            user.getApplications().add(this);
        }
    }
}