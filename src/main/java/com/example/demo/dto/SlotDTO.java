package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter // Generates getters and setters for all fields
public class SlotDTO {

    private Long id;

    private Long uslugaId; // Reference to the associated Usluga

    private Date date;

    private Time time;

    private boolean isAvailable = true;
}