package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Usluga {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String category;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;
    private String location;
    private String coordinates;
    @OneToMany(mappedBy="usluga", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Slot> slots=new ArrayList<>();
    private double price;
    private int durationMinutes;

    public Usluga() {
        super();
        // TODO Auto-generated constructor stub
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

    public String getName(){
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}

    public String getCategory(){return category;}
    public void setCategory(String category){this.category=category;}
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getLocation(){return location;}
    public void setLocation(String location){this.location=location;}
    public String getCoordinates(){return coordinates;}
    public void setCoordinates(String coordinates){this.coordinates=coordinates;}

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    public List<Slot> getSlots(){return slots;}
    public void setSlots(List<Slot> slots){this.slots=slots;}
}

