package com.example.demo.repository;

import com.example.demo.model.ModerationStatus;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // для публичных запросов — только одобренные
    List<Review> findByStatus(ModerationStatus status);

    List<Review> findByReviewedUser(User user);

    List<Review> findByReviewedUserAndStatus(User user, ModerationStatus status);
}