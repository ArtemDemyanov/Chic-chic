package com.example.demo.controller;

import com.example.demo.dto.PortfolioDTO;
import com.example.demo.mapper.PortfolioMapper;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
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
    public ResponseEntity<?> createPortfolio(@PathVariable Long userId, @RequestBody PortfolioDTO portfolioDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Portfolio portfolio = PortfolioMapper.toEntity(portfolioDTO);
            User user = optionalUser.get();
            portfolio.setUser(user);
            Portfolio savedPortfolio = portfolioRepository.save(portfolio);
            return ResponseEntity.ok(PortfolioMapper.toDTO(savedPortfolio));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*@Operation(summary = "Посмотреть портфолио определенного мастера")
    @GetMapping("/portfolio/{id}")
    public ResponseEntity<Optional<PortfolioDTO>> getPortfolioByMasterId(@PathVariable(value = "id") long masterId) {
        Optional<PortfolioDTO> portfolioDTO = portfolioService.findPortfolioByUserId(masterId).map(PortfolioMapper::toDTO);
        return ResponseEntity.ok(portfolioDTO);
    }*/

    @Operation(summary = "Посмотреть портфолио определенного мастера")
    @GetMapping("/portfolio/{id}")
    public ResponseEntity<PortfolioDTO> getPortfolioByMasterId(
            @PathVariable(value = "id") long masterId,
            Principal principal) {

        // 1) по Principal.getName() (email/login) находим пользователя
        User me = userRepository.findByEmail(principal.getName());

        if (me == null) {
            throw new UsernameNotFoundException(
                    "User not found: " + principal.getName()
            );
        }

        Long requesterId = me.getId();
        boolean isAdmin = me.getRole().equals("ROLE_ADMIN");

        // 3) Берём из сервиса
        Optional<Portfolio> opt = portfolioService.findForViewer(masterId, requesterId, isAdmin);

        // 4) Маппим и возвращаем
        return opt
                .map(PortfolioMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить портфолио")
    @DeleteMapping("/portfolio/{id}")
    public String deletePortfolio(@PathVariable(value = "id") long Id) {
        portfolioService.deletePortfolio(Id);
        return "Portfolio Deleted";
    }
    //не получилось протестировать через Postman, Катя пожалуйста помоги
    /*@PutMapping(path = "/portfolio/{id}", consumes = "multipart/form-data")
    public ResponseEntity<PortfolioDTO> updatePortfolio(
            @PathVariable(value = "id") Long id,
            @RequestPart("portfolioDTO") PortfolioDTO portfolioDTO,
            @RequestPart("photos") List<MultipartFile> files) {

        // Находим существующее портфолио по ID
        Optional<Portfolio> existingPortfolioOptional = portfolioService.findByID(id);
        if (!existingPortfolioOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Получаем существующее портфолио
        Portfolio portfolioToUpdate = existingPortfolioOptional.get();

        // Обработка файлов (если они есть)
        List<byte[]> photos = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        photos.add(file.getBytes());
                    } catch (IOException e) {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }
        }

        // Преобразуем DTO в сущность Portfolio
        Portfolio updatedPortfolio = PortfolioMapper.toEntity(portfolioDTO);
        updatedPortfolio.setId(portfolioToUpdate.getId()); // Сохраняем существующий ID
        updatedPortfolio.setPhotosAsList(photos); // Устанавливаем новые фото
        updatedPortfolio.setDescription(portfolioDTO.getDescription()); // Обновляем описание

        // Сохраняем обновленное портфолио
        Portfolio savedPortfolio = portfolioService.savePortfolio(updatedPortfolio);

        // Преобразуем сохраненное портфолио обратно в DTO и возвращаем клиенту
        PortfolioDTO responseDTO = PortfolioMapper.toDTO(savedPortfolio);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }*/

    //Работает, но фотки не отображаются, скорее всего наш косяк, Катя помоги
    @PutMapping(path = "/portfolio/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updatePortfolio(
            @PathVariable(value = "id") long Id,
            @RequestPart("photos") List<MultipartFile> files,
            @RequestPart("description") String description) {

        // Fetch the existing portfolio by ID
        Optional<Portfolio> existingPortfolio = portfolioService.findByID(Id);

        if (!existingPortfolio.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Map the existing portfolio to DTO
        Portfolio portfolioToUpdate = existingPortfolio.get();

        // Collect the photos from the MultipartFile
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

        // Update the fields of the portfolio with new data
        portfolioToUpdate.setPhotosAsList(photos);
        portfolioToUpdate.setDescription(description);

        // Save the updated portfolio entity
        portfolioService.savePortfolio(portfolioToUpdate);

        // Map the updated portfolio entity back to DTO
        PortfolioDTO updatedPortfolioDTO = PortfolioMapper.toDTO(portfolioToUpdate);

        return new ResponseEntity<>(updatedPortfolioDTO, HttpStatus.OK);
    }

}