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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    public Optional<Portfolio> getPortfolioByMasterId(@PathVariable(value = "id") long masterId) {
        //Optional<Portfolio> portfolio = portfolioService.findPortfolioByUserId(masterId);
        return portfolioService.findPortfolioByUserId(masterId);
    }

    @Operation(summary = "Удалить портфолио")
    @DeleteMapping("/portfolio/{id}")
    public String deletePortfolio(@PathVariable(value = "id") long Id) {
        portfolioService.deletePortfolio(Id);
        return "Portfolio Deleted";
    }

   /* @Operation(summary = "Обновить портфолио")
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
    } */

    @PutMapping(path = "/portfolio/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updatePortfolio(
            @PathVariable(value = "id") long Id,
            @RequestPart("photos") List<MultipartFile> files,
            @RequestPart("description") String description) {

        Optional<Portfolio> existingPortfolio = portfolioService.findByID(Id);

        if (!existingPortfolio.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Portfolio portfolioToUpdate = existingPortfolio.get();
        List<byte[]> photos = new ArrayList<>();


        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                try {
                    photos.add(file.getBytes());
                } catch (IOException e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }

        portfolioToUpdate.setPhotosAsList(photos);
        portfolioToUpdate.setDescription(description);
        portfolioService.savePortfolio(portfolioToUpdate);
        return new ResponseEntity<>(portfolioToUpdate, HttpStatus.OK);
    }

}