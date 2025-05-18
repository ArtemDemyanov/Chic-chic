package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UslugaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final ReviewRepository reviewRepo;
    private final PortfolioRepository portfolioRepo;
    private final UslugaRepository uslugaRepo;
    private final UserRepository userRepo;

    public AdminService(ReviewRepository reviewRepo, PortfolioRepository portfolioRepo, UslugaRepository uslugaRepo, UserRepository userRepo) {
        this.reviewRepo = reviewRepo;
        this.portfolioRepo = portfolioRepo;
        this.uslugaRepo = uslugaRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void approveReview(Long reviewId) {
        Review r = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        r.setStatus(ModerationStatus.APPROVED);
    }

    @Transactional
    public void rejectReview(Long reviewId) {
        Review r = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        // вариант с удалением:
        reviewRepo.delete(r);
        // или, чтобы хранить историю:
        // r.setStatus(ModerationStatus.REJECTED);
    }

    @Transactional
    public void approvePortfolio(Long portfolioId) {
        Portfolio p = portfolioRepo.findById(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found"));
        p.setStatus(ModerationStatus.APPROVED);
    }

    @Transactional
    public void rejectPortfolio(Long portfolioId) {
        Portfolio p = portfolioRepo.findById(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found"));
        // вариант с удалением
        portfolioRepo.delete(p);
        // или чтобы хранить историю и отображать логи
        // p.setStatus(ModerationStatus.REJECTED)
    }

    @Transactional
    public void approveUsluga(Long uslugaId) {
        Usluga u = uslugaRepo.findById(uslugaId)
                .orElseThrow(() -> new EntityNotFoundException("Usluga not found"));
        u.setStatus(ModerationStatus.APPROVED);
    }

    @Transactional
    public void rejectUsluga(Long uslugaId) {
        Usluga u = uslugaRepo.findById(uslugaId)
                .orElseThrow(() -> new EntityNotFoundException("Usluga not found"));
        // вариант с удалением:
        uslugaRepo.delete(u);
        // или, чтобы хранить историю:
        // r.setStatus(ModerationStatus.REJECTED);
    }

    public void banUser(Long userId, boolean isBanned) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        user.setBanned(isBanned);
        userRepo.save(user);
    }
}
