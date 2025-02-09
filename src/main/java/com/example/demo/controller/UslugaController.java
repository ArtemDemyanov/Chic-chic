package com.example.demo.controller;

import com.example.demo.dto.UslugaDTO;
import com.example.demo.mapper.UslugaMapper;
import com.example.demo.model.User;
import com.example.demo.model.Usluga;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UslugaRepository;
import com.example.demo.service.UslugaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usluga")
public class UslugaController {

    private final UslugaRepository uslugaRepository;
    private final UserRepository userRepository;
    private final UslugaService uslugaService;

    @Autowired
    public UslugaController(UslugaRepository uslugaRepository, UserRepository userRepository, UslugaService uslugaService) {
        this.uslugaRepository = uslugaRepository;
        this.userRepository = userRepository;
        this.uslugaService = uslugaService;
    }

    @Operation(summary = "Добавить услугу")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @PostMapping(path = "/create/{userId}")
    public ResponseEntity<UslugaDTO> createUsluga(@PathVariable Long userId, @RequestBody UslugaDTO uslugaDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Usluga usluga = UslugaMapper.toEntity(uslugaDTO);
            usluga.setUser(optionalUser.get());
            Usluga savedUsluga = uslugaRepository.save(usluga);
            return ResponseEntity.ok(UslugaMapper.toDTO(savedUsluga));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Обновить услугу")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @PutMapping("/{id}")
    public ResponseEntity<UslugaDTO> updateUsluga(@PathVariable(value = "id") long id, @RequestBody UslugaDTO uslugaDTO) {
        Optional<Usluga> existingUslugaOptional = uslugaService.findByID(id);
        if (existingUslugaOptional.isPresent()) {
            Usluga existingUsluga = existingUslugaOptional.get();
            Usluga updatedUsluga = UslugaMapper.toEntity(uslugaDTO);
            updatedUsluga.setUser(existingUsluga.getUser()); // Preserve the existing user association
            Usluga savedUsluga = uslugaService.saveUsluga(updatedUsluga);
            return ResponseEntity.ok(UslugaMapper.toDTO(savedUsluga));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Посмотреть все услуги")
    @GetMapping("/all")
    public ResponseEntity<List<UslugaDTO>> getAllUslugas() {
        List<Usluga> uslugas = uslugaRepository.findAll();
        List<UslugaDTO> uslugaDTOs = uslugas.stream()
                .map(UslugaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(uslugaDTOs);
    }

    @Operation(summary = "Посмотреть услуги определенного мастера")
    @GetMapping("/{userId}")
    public ResponseEntity<List<UslugaDTO>> getUslugasByUserId(@PathVariable Long userId) {
        List<Usluga> uslugas = uslugaService.getUslugas(userId);
        List<UslugaDTO> uslugaDTOs = uslugas.stream()
                .map(UslugaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(uslugaDTOs);
    }

    @Operation(summary = "Удалить услугу")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @DeleteMapping("/{id}")
    public String deleteUsluga(@PathVariable(value = "id") long id) {
        uslugaService.deleteUsluga(id);
        return "Usluga Deleted";
    }

    @Operation(summary = "Поиск услуги по имени")
    @GetMapping("/search")
    public ResponseEntity<List<UslugaDTO>> searchUslugasByName(@RequestParam String name) {
        List<Usluga> uslugas = uslugaRepository.findByNameContainingIgnoreCase(name);
        List<UslugaDTO> uslugaDTOs = uslugas.stream()
                .map(UslugaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(uslugaDTOs);
    }

    @Operation(summary = "Поиск услуги по локации")
    @GetMapping("/city")
    public ResponseEntity<List<UslugaDTO>> getUslugasByLocation(@RequestParam String location) {
        List<Usluga> uslugas = uslugaRepository.findByLocation(location);
        List<UslugaDTO> uslugaDTOs = uslugas.stream()
                .map(UslugaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(uslugaDTOs);
    }
}