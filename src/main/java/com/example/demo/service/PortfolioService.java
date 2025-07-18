package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ModerationStatus;
import com.example.demo.model.Portfolio;
import com.example.demo.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository portfolioRepository;

    public PortfolioService(){
        super();
    }

//    public List<Portfolio> getPortfolios() {
//        return (List<Portfolio>) portfolioRepository.findAll();
//    }

    public Optional<Portfolio> findByID(Long id) {
        return portfolioRepository.findById(id);
    }

    public Optional<Portfolio> findPortfolioByUserId(Long userId) {
        return portfolioRepository.findByUserId(userId);
    }

    public Portfolio savePortfolio(Portfolio newPortfolio){
        return portfolioRepository.save(newPortfolio);
    }

    public Optional<Portfolio> findById(Long id){
        return portfolioRepository.findById(id);
    }

    public void deletePortfolio(Long id){
        Portfolio portfolio = findById(id).orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", id));
        portfolioRepository.delete(portfolio);
    }

    //может потом исправить так, чтобы каждую фотку в отдельности проверять
    public List<Portfolio> findAllPortfolio(){
        return portfolioRepository.findByStatus(ModerationStatus.APPROVED);
    }

    public Optional<Portfolio> findApprovedPortfolioByUserId(Long userId) {
        return portfolioRepository.findByUserIdAndStatus(userId, ModerationStatus.APPROVED);
    }

    public Optional<Portfolio> findForViewer(Long masterId, Long requesterId, boolean isAdmin) {
        if (isAdmin || Objects.equals(masterId, requesterId)) {
            return portfolioRepository.findByUserId(masterId);
        } else {
            return portfolioRepository.findByUserIdAndStatus(masterId, ModerationStatus.APPROVED);
        }
    }
}
