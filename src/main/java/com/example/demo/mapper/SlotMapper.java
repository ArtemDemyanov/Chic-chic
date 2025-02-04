package com.example.demo.mapper;

import com.example.demo.dto.SlotDTO;
import com.example.demo.model.Slot;

public class SlotMapper {
    public static SlotDTO toDTO(Slot slot) {
        SlotDTO dto = new SlotDTO();
        dto.setId(slot.getId());
        dto.setUslugaId(slot.getUsluga() != null ? slot.getUsluga().getId() : null); // Map only the Usluga ID
        dto.setDate(slot.getDate());
        dto.setTime(slot.getTime());
        dto.setAvailable(slot.isAvailable());
        return dto;
    }

    // Convert SlotDTO to Slot entity
    public static Slot toEntity(SlotDTO dto) {
        Slot slot = new Slot();
        slot.setId(dto.getId());
        slot.setDate(dto.getDate());
        slot.setTime(dto.getTime());
        slot.setAvailable(dto.isAvailable());
        return slot;
    }
}
