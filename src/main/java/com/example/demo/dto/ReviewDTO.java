package com.example.demo.dto;

import com.example.demo.model.ModerationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {
    private Long id;
    private String content;
    private Long reviewerId;
    private Long reviewedUserId;
    private ModerationStatus moderationStatus;
}