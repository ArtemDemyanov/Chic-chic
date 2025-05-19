package com.example.demo.dto;

import com.example.demo.model.ModerationStatus;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class UslugaDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Long userId; // Reference to the user instead of the User object
    private String location;
    private String coordinates;
    private List<SlotDTO> slots; // Use SlotDTO instead of Slot
    private double price;
    private int durationMinutes;
    private ModerationStatus status;

}