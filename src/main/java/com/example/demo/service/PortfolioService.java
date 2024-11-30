package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Portfolio;
import com.example.demo.model.User;
import com.example.demo.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository portfolioRepository;

    public PortfolioService(){
        super();
    }

    public List<Portfolio> getPortfolios() {
        return (List<Portfolio>) portfolioRepository.findAll();
    }
    public Optional<Portfolio> findByID(Long id) {
        return portfolioRepository.findById(id);
    }

    public void savePortfolio(Portfolio newPortfolio){
        portfolioRepository.save(newPortfolio);
    }

    public Optional<Portfolio> findById(Long id){
        return portfolioRepository.findById(id);
    }

    public void deletePortfolio(Long id){
        Portfolio portfolio = findById(id).orElseThrow(() -> new ResourceNotFoundException("Portfolio", "id", id));
        portfolioRepository.delete(portfolio);
    }

    public Portfolio findByUserId(Long id){
        return portfolioRepository.findByUserId(id);
    }
}
