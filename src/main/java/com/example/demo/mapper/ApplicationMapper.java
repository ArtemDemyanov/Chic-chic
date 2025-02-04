package com.example.demo.mapper;

import com.example.demo.dto.ApplicationDTO;
import com.example.demo.model.Application;

public class ApplicationMapper {

    // Convert Application entity to ApplicationDTO
    public static ApplicationDTO toDTO(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        dto.setUslugaId(application.getUsluga() != null ? application.getUsluga().getId() : null);
        dto.setApplicantName(application.getApplicantName());
        dto.setUslugaName(application.getUslugaName());
        dto.setApplicantEmail(application.getApplicantEmail());
        dto.setDate(application.getDate());
        dto.setTime(application.getTime());
        dto.setSlotId(application.getSlot() != null ? application.getSlot().getId() : null);
        dto.setUserId(application.getUser() != null ? application.getUser().getId() : null);
        return dto;
    }

    // Convert ApplicationDTO to Application entity
    public static Application toEntity(ApplicationDTO dto) {
        Application application = new Application();
        application.setId(dto.getId());
        application.setApplicantName(dto.getApplicantName());
        application.setUslugaName(dto.getUslugaName());
        application.setApplicantEmail(dto.getApplicantEmail());
        application.setDate(dto.getDate());
        application.setTime(dto.getTime());
        return application;
    }
}