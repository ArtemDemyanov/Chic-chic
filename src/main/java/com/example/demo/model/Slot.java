package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Slot {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="usluga_id")
    @JsonBackReference
    private Usluga usluga;
    private Date date;
    @ElementCollection
    @CollectionTable(name = "slot_times", joinColumns = @JoinColumn(name = "slot_id"))
    @Column(name = "time")
    private List<Time> time = new ArrayList<>();



    public Slot() {
        super();
        // TODO Auto-generated constructor stub
    }


    public Slot(Date date, List<Time> time) {
        super();
        this.date = date;
        this.time=time;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public Date getDate(){return date;}
    public void setDate(Date date){this.date=date;}
    public List<Time> getTime(){return time;}
    public void setTime(List<Time> time){this.time=time;}
    public Usluga getUsluga(){return usluga;}
    public void setUsluga(Usluga usluga){this.usluga=usluga;}
}

