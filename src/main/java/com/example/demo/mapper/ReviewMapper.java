package com.example.demo.mapper;

import com.example.demo.dto.ReviewDTO;
import com.example.demo.model.Review;

public class ReviewMapper {

    public static ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setReviewerId(review.getReviewer().getId());
        dto.setReviewedUserId(review.getReviewedUser().getId());
        dto.setModerationStatus(review.getStatus());
        return dto;
    }

    public static Review toEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setContent(reviewDTO.getContent());
        review.setStatus(reviewDTO.getModerationStatus());
        return review;
    }
}
