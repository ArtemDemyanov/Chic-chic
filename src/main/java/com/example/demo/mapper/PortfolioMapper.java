package com.example.demo.mapper;

import com.example.demo.dto.PortfolioDTO;
import com.example.demo.model.Portfolio;

public class PortfolioMapper {
    public static PortfolioDTO toDTO(Portfolio portfolio) {
        PortfolioDTO dto = new PortfolioDTO();
        dto.setId(portfolio.getId());
        dto.setUserId(portfolio.getUser().getId());
        dto.setExperience(portfolio.getExperience());
        dto.setCertificates(portfolio.getCertificates());
        dto.setPhotos(portfolio.getPhotosAsList());
        dto.setDescription(portfolio.getDescription());
        return dto;
    }

    public static Portfolio toEntity(PortfolioDTO dto) {
        Portfolio portfolio = new Portfolio();
        portfolio.setExperience(dto.getExperience());
        portfolio.setCertificates(dto.getCertificates());
        portfolio.setPhotosAsList(dto.getPhotos());
        portfolio.setDescription(dto.getDescription());
        return portfolio;
    }
}