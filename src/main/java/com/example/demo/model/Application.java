package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "applications")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Application {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @lombok.Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name="usluga_id")
    @JsonIgnoreProperties("applications")
    @lombok.Setter
    @lombok.Getter
    private Usluga usluga;

    @lombok.Setter
    @lombok.Getter
    private String applicantName;

    @lombok.Setter
    @lombok.Getter
    private String uslugaName;

    @lombok.Setter
    @lombok.Getter
    private String applicantEmail;

    @lombok.Setter
    @lombok.Getter
    private Date date;

    @lombok.Setter
    @lombok.Getter
    private Time time;

    @OneToOne
    @JoinColumn(name="slot_id")
    @lombok.Getter @lombok.Setter
    private Slot slot;

    @ManyToOne
    @JoinColumn(name="user_id")
    @lombok.Getter @lombok.Setter
    private User user;

    public Application() {
        super();
    }

    public Application(String applicantName, String uslugaName, String applicantEmail, Usluga usluga, User user) {
        super();
        this.usluga = usluga;
        this.applicantName = applicantName;
        this.uslugaName = uslugaName;
        this.applicantEmail = applicantEmail;
        this.user = user;
    }
}
