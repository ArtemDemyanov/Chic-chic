package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.sql.Time;
import java.util.Date;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Application {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="usluga_id")
    @JsonIgnoreProperties("applications")
    private Usluga usluga;
    private String applicantName;
    private String uslugaName;
    private String applicantEmail;
    private Date date;

    private Time time;
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;
    public Application() {
        super();
        // TODO Auto-generated constructor stub
    }
    public Application(String applicantName, String uslugaName, String applicantEmail, Usluga usluga, User user) {
        super();
        this.usluga=usluga;
        this.applicantName=applicantName;
        this.uslugaName=uslugaName;
        this.applicantEmail=applicantEmail;
        this.user=user;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public Usluga getUsluga(){
        return usluga;
    }
    public void setUsluga(Usluga event){
        this.usluga=usluga;
    }
    public String getApplicantName(){
        return applicantName;
    }
    public void setApplicantName(String applicantName){
        this.applicantName=applicantName;
    }
    public String getUslugaName(){
        return uslugaName;
    }
    public void setUslugaName(String uslugaName){
        this.uslugaName=uslugaName;
    }
    public String getApplicantEmail(){return applicantEmail;}
    public void setApplicantEmail(String applicantEmail){this.applicantEmail=applicantEmail;}
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user=user;
    }
    public Date getDate(){return date;}
    public void setDate(Date date){this.date=date;}
    public Time getTime(){return time;}
    public void setTime(Time time){this.time=time;}
}
