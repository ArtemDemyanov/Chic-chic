package com.example.demo.controller;

import com.example.demo.model.Portfolio;
import com.example.demo.model.User;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@PreAuthorize("hasRole('ROLE_MASTER')")
@RestController
public class PortfolioController {
    private final PortfolioService portfolioService;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    @Autowired
    public PortfolioController(PortfolioService portfolioService, UserRepository userRepository, PortfolioRepository portfolioRepository) {
        this.portfolioService = portfolioService;
        this.userRepository = userRepository;
        this.portfolioRepository = portfolioRepository;
    }
    @Operation(summary = "Посмотреть все портфолио")
    @GetMapping("/portfolio")
    public List<Portfolio> getPortfolios() {
        return portfolioService.getPortfolios();
    }
    @Operation(summary = "Создать портфолио")
    @PostMapping(path="/create/{userId}")
    public ResponseEntity<?> createPortfolio(@PathVariable Long userId, @RequestBody Portfolio portfolio) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            portfolio.setUser(user);
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            return ResponseEntity.ok(savedPortfolio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Посмотреть портфолио определенного мастера")
    @GetMapping("/portfolio/{id}")
    public Optional<Portfolio> getPortfolioById(@PathVariable(value = "id") long Id) {
        return portfolioService.findByID(Id);
    }
    @Operation(summary = "Удалить портфолио")
    @DeleteMapping("/portfolio/{id}")
    public String deletePortfolio(@PathVariable(value = "id") long Id) {
        portfolioService.deletePortfolio(Id);
        return "Portfolio Deleted";
    }
    @Operation(summary = "Обновить портфолио")
    @PutMapping("/portfolio/{id}")
    public ResponseEntity<Optional<Portfolio>> updatePortfolio(@PathVariable(value="id") long Id, @RequestBody Portfolio newPortfolio ){
        Optional<Portfolio> existingPortfolio = portfolioService.findByID(Id);
        if (existingPortfolio.isPresent()) {
            Portfolio portfolioToUpdate = existingPortfolio.get();
            portfolioToUpdate.setPhotos(newPortfolio.getPhotos());
            portfolioToUpdate.setDescription(newPortfolio.getDescription());
            portfolioService.savePortfolio(portfolioToUpdate);
            return new ResponseEntity<>(Optional.ofNullable(portfolioToUpdate), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Optional.empty(), HttpStatus.NOT_FOUND);
        }
    }
}
