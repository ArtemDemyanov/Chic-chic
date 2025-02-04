package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
public class ApplicationDTO {

    private Long id;

    private Long uslugaId; // Reference to the associated Usluga

    private String applicantName;

    private String uslugaName;

    private String applicantEmail;

    private Date date;

    private Time time;

    private Long slotId; // Reference to the associated Slot

    private Long userId; // Reference to the associated User
}