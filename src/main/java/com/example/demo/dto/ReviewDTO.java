package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private String content;
    private Long reviewerId;
    private Long reviewedUserId;
}