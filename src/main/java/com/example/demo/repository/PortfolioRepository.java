package com.example.demo.repository;

import com.example.demo.model.ModerationStatus;
import com.example.demo.model.Portfolio;
import com.example.demo.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long > {

    Optional<Portfolio> findByUserId(Long userId);

    // для публичных запросов — только одобренные
    List<Portfolio> findByStatus(ModerationStatus status);

    // для личного кабинета — свои все, вне зависимости от статуса
    List<Portfolio> findByAuthorId(Long authorId);

    // для админа — все или выборочно по статусу
    List<Portfolio> findByStatusIn(List<ModerationStatus> statuses);

    Optional<Portfolio> findByUserIdAndStatus(Long userId, ModerationStatus status);
}
