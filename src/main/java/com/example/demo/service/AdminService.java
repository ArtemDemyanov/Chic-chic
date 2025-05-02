package com.example.demo.service;

import com.example.demo.model.ModerationStatus;
import com.example.demo.model.Portfolio;
import com.example.demo.model.Review;
import com.example.demo.model.Usluga;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UslugaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final ReviewRepository reviewRepo;
    private final PortfolioRepository portfolioRepo;
    private final UslugaRepository uslugaRepo;

    public AdminService(ReviewRepository reviewRepo, PortfolioRepository portfolioRepo, UslugaRepository uslugaRepo) {
        this.reviewRepo = reviewRepo;
        this.portfolioRepo = portfolioRepo;
        this.uslugaRepo = uslugaRepo;
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
}
