package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PortfolioDTO {
    private Long id;
    private int experience;
    private String description;
    private List<byte[]> certificates;
    private byte[] photos;
    private Long userId;
}