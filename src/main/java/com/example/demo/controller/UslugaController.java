package com.example.demo.controller;

import com.example.demo.model.Slot;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @PostMapping(path="/create/{userId}")
    public ResponseEntity<?> createUsluga(@PathVariable Long userId, @RequestBody Usluga usluga) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            usluga.setUser(user);
            Usluga savedUsluga = uslugaRepository.save(usluga);
            return ResponseEntity.ok(savedUsluga);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usluga> updateUsluga(@PathVariable(value="id") long id, @RequestBody Usluga newUsluga) {
        Optional<Usluga> existingUslugaOptional = uslugaService.findByID(id);
        if (existingUslugaOptional.isPresent()) {
            Usluga existingUsluga = existingUslugaOptional.get();

            // Обновление основных свойств услуги
            existingUsluga.setName(newUsluga.getName());
            existingUsluga.setDescription(newUsluga.getDescription());
            existingUsluga.setCategory(newUsluga.getCategory());
            existingUsluga.setCoordinates(newUsluga.getCoordinates());
            existingUsluga.setLocation(newUsluga.getLocation());
            existingUsluga.setPrice(newUsluga.getPrice());
            existingUsluga.setDurationMinutes(newUsluga.getDurationMinutes());

            // Обработка списка слотов
            List<Slot> currentSlots = existingUsluga.getSlots();
            List<Slot> newSlots = newUsluga.getSlots();

            if (newSlots == null || newSlots.isEmpty()) {
                // Если новый список слотов пуст, удаляем все существующие слоты
                for (Slot currentSlot : currentSlots) {
                    currentSlot.setUsluga(null); // Разрываем связь
                }
                currentSlots.clear(); // Очищаем коллекцию
            } else {
                // Удаление отсутствующих слотов
                List<Slot> slotsToRemove = new ArrayList<>();
                for (Slot currentSlot : currentSlots) {
                    if (!newSlots.contains(currentSlot)) {
                        currentSlot.setUsluga(null); // Разрываем связь
                        slotsToRemove.add(currentSlot);
                    }
                }
                currentSlots.removeAll(slotsToRemove);

                // Добавление новых слотов
                for (Slot newSlot : newSlots) {
                    if (!currentSlots.contains(newSlot)) {
                        newSlot.setUsluga(existingUsluga); // Устанавливаем связь
                        currentSlots.add(newSlot);
                    }
                }
            }

            uslugaService.saveUsluga(existingUsluga);
            return ResponseEntity.ok(existingUsluga);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Посмотреть все услуги")
    @GetMapping("/all")
    public ResponseEntity<List<Usluga>> getAllUslugas() {
        List<Usluga> Uslugas = uslugaRepository.findAll();
        return ResponseEntity.ok(Uslugas);
    }

    @Operation(summary = "Посмотреть услуги определнного мастера")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<Usluga>> getUslugasByUserId(@PathVariable Long userId) {
        List<Usluga> uslugas = uslugaService.getUslugas(userId);
        return ResponseEntity.ok(uslugas);
    }

    @Operation(summary = "Удалить услугу")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @DeleteMapping("/{id}")
    public String deleteUsluga(@PathVariable(value = "id") long Id) {
        uslugaService.deleteUsluga(Id);
        return "Usluga Deleted";
    }

    @Operation(summary = "Поиск услуги по имени")
    @GetMapping("/search")
    public ResponseEntity<List<Usluga>> searchUslugasByName(@RequestParam String name) {
        List<Usluga> uslugas = uslugaRepository.findByNameContainingIgnoreCase(name);
        return ResponseEntity.ok(uslugas);
    }

    @Operation(summary = "Поиск услуги по локации (название адреса и т. д.)")
    @GetMapping("/city")
    public ResponseEntity<List<Usluga>> getUslugasByLocation(@RequestParam String location) {
        List<Usluga> Uslugas = uslugaRepository.findByLocation(location);
        return ResponseEntity.ok(Uslugas);
    }
}
