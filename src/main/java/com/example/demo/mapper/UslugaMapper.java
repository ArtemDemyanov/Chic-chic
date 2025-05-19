package com.example.demo.mapper;

import com.example.demo.dto.SlotDTO;
import com.example.demo.dto.UslugaDTO;
import com.example.demo.model.Slot;
import com.example.demo.model.Usluga;

import java.util.stream.Collectors;

public class UslugaMapper {

    // Convert Usluga entity to UslugaDTO
    public static UslugaDTO toDTO(Usluga usluga) {
        UslugaDTO dto = new UslugaDTO();
        dto.setId(usluga.getId());
        dto.setName(usluga.getName());
        dto.setDescription(usluga.getDescription());
        dto.setCategory(usluga.getCategory());
        dto.setUserId(usluga.getUser() != null ? usluga.getUser().getId() : null); // Map only the user ID
        dto.setLocation(usluga.getLocation());
        dto.setCoordinates(usluga.getCoordinates());
        dto.setPrice(usluga.getPrice());
        dto.setDurationMinutes(usluga.getDurationMinutes());
        dto.setStatus(usluga.getStatus());

        // Map slots
        if (usluga.getSlots() != null) {
            dto.setSlots(usluga.getSlots().stream()
                    .map(SlotMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    // Convert UslugaDTO to Usluga entity
    public static Usluga toEntity(UslugaDTO dto) {
        Usluga usluga = new Usluga();
        usluga.setId(dto.getId());
        usluga.setName(dto.getName());
        usluga.setDescription(dto.getDescription());
        usluga.setCategory(dto.getCategory());
        usluga.setLocation(dto.getLocation());
        usluga.setCoordinates(dto.getCoordinates());
        usluga.setPrice(dto.getPrice());
        usluga.setDurationMinutes(dto.getDurationMinutes());
        usluga.setStatus(dto.getStatus());

        // Map slots
        if (dto.getSlots() != null) {
            usluga.setSlots(dto.getSlots().stream()
                    .map(slotDTO -> {
                        Slot slot = SlotMapper.toEntity(slotDTO);
                        slot.setUsluga(usluga); // Set the bidirectional relationship
                        return slot;
                    })
                    .collect(Collectors.toList()));
        }

        return usluga;
    }
}