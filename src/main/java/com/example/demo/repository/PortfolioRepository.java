package com.example.demo.repository;

import com.example.demo.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long > {

    Portfolio findByUserId(Long userId);
}