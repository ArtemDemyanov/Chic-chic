package com.example.demo.controller;

import com.example.demo.dto.UslugaDTO;
import com.example.demo.mapper.SlotMapper;
import com.example.demo.mapper.UslugaMapper;
import com.example.demo.model.ModerationStatus;
import com.example.demo.model.Slot;
import com.example.demo.model.User;
import com.example.demo.model.Usluga;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UslugaRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.UslugaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Map;
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

    /*@Operation(summary = "Обновить услугу")
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
    }*/

    @Transactional
    @Operation(summary = "Обновить услугу")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @PutMapping("/{id}")
    public ResponseEntity<UslugaDTO> updateUsluga(@PathVariable(value = "id") long id, @RequestBody UslugaDTO dto) {
        Usluga existing = uslugaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Услуга с id " + id + " не найдена"));

        // Обновляем базовые поля
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setCategory(dto.getCategory());
        existing.setLocation(dto.getLocation());
        existing.setCoordinates(dto.getCoordinates());
        existing.setPrice(dto.getPrice());
        existing.setDurationMinutes(dto.getDurationMinutes());

        // Обновляем слоты
        if (dto.getSlots() != null) {
            Map<Long, Slot> existingSlotsMap = existing.getSlots().stream()
                    .collect(Collectors.toMap(Slot::getId, slot -> slot));

            List<Slot> updatedSlots = dto.getSlots().stream()
                    .map(slotDTO -> {
                        if (slotDTO.getId() != null && existingSlotsMap.containsKey(slotDTO.getId())) {
                            // Слот существует — обновляем
                            Slot existingSlot = existingSlotsMap.get(slotDTO.getId());
                            existingSlot.setDate(slotDTO.getDate());
                            existingSlot.setTime(slotDTO.getTime());
                            if (slotDTO.getIsAvailable() != null) {
                                existingSlot.setIsAvailable(slotDTO.getIsAvailable());
                            }
                            return existingSlot;
                        } else {
                            // Новый слот — создаём
                            Slot newSlot = SlotMapper.toEntity(slotDTO);
                            newSlot.setUsluga(existing);
                            return newSlot;
                        }
                    })
                    .collect(Collectors.toList());

            existing.getSlots().clear();
            existing.getSlots().addAll(updatedSlots);
        }

        Usluga saved = uslugaRepository.save(existing);
        return ResponseEntity.ok(UslugaMapper.toDTO(saved));
    }

    /*@Operation(summary = "Посмотреть все услуги")
    @GetMapping("/all")
    public ResponseEntity<List<UslugaDTO>> getAllUslugas() {
        List<Usluga> uslugas = uslugaRepository.findAll();
        List<UslugaDTO> uslugaDTOs = uslugas.stream()
                .map(UslugaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(uslugaDTOs);
    }*/

    @Operation(summary = "Посмотреть все услуги")
    @GetMapping("/all")
    public ResponseEntity<List<UslugaDTO>> getAllUslugas() {
        List<Usluga> uslugas = uslugaRepository.findByStatus(ModerationStatus.APPROVED);;
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