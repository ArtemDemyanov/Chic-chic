package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PortfolioDTO {
    private Long id;
    private Long userId;
    private int experience;
    private List<byte[]> certificates;
    private List<byte[]> photos;
    private String description;
}