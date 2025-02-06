package com.example.demo.controller;

import com.example.demo.dto.ApplicationDTO;
import com.example.demo.mapper.ApplicationMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Application;
import com.example.demo.model.Slot;
import com.example.demo.model.User;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.SlotRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final SlotRepository slotRepository;
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationRepository applicationRepository, UserRepository userRepository,
                                 SlotRepository slotRepository, ApplicationService applicationService) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.slotRepository = slotRepository;
        this.applicationService = applicationService;
    }

    @Operation(summary = "Запись на услугу")
    @PostMapping("/{userId}/{slotId}")
    public ResponseEntity<?> applyForUsluga(@PathVariable Long userId, @PathVariable Long slotId, @RequestBody ApplicationDTO applicationDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Slot> optionalSlot = slotRepository.findById(slotId);

        if (optionalUser.isPresent() && optionalSlot.isPresent()) {
            Slot slot = optionalSlot.get();
            if (!slot.isAvailable()) {
                return ResponseEntity.badRequest().body("Этот слот уже занят.");
            }

            User user = optionalUser.get();
            Application application = ApplicationMapper.toEntity(applicationDTO);

            // Set relationships
            application.setUsluga(slot.getUsluga());
            application.setUslugaName(slot.getUsluga().getName());
            application.setUser(user);
            application.setMaster(slot.getUsluga().getUser());
            application.setApplicantName(user.getName());
            application.setApplicantEmail(user.getEmail());
            application.setSlot(slot);

            // Update bidirectional relationship
            user.addApplication(application); // Use the helper method

            // Mark the slot as unavailable
            slot.setAvailable(false);
            slotRepository.save(slot);

            // Save the application
            Application savedApplication = applicationRepository.save(application);
            return new ResponseEntity<>(ApplicationMapper.toDTO(savedApplication), HttpStatus.CREATED);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Посмотреть записи на определенную услугу")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @GetMapping("/usluga/{uslugaId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsForUsluga(@PathVariable Long uslugaId) {
        List<Application> applications = applicationService.getApplicationsUsluga(uslugaId);
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(ApplicationMapper::toDTO)
                .toList();
        return ResponseEntity.ok(applicationDTOs);
    }

    //было /master/{userId} стало /user/{userId}
    @Operation(summary = "Посмотреть записи пользователя")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsForUser(@PathVariable Long userId) {
        List<Application> applications = applicationService.getApplicationsUser(userId);
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(ApplicationMapper::toDTO)
                .toList();
        return ResponseEntity.ok(applicationDTOs);
    }

    @Operation(summary = "Удалить запись")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> withdrawApplication(@PathVariable(value = "id") long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);

        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            Slot slot = application.getSlot();

            if (slot != null) {
                slot.setAvailable(true); // Mark the slot as available
                slotRepository.save(slot);
            }

            applicationRepository.delete(application);
            return ResponseEntity.ok("Application withdrawn and slot made available");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //новый метод, через него гетается записи к мастеру и оттуда слоты, на которые записались, для расписания
    @Operation(summary = "Мастер смотрит записи на его услуги")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @GetMapping("/master/{masterId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByMaster(@PathVariable Long masterId) {
        List<Application> applications = applicationService.getApplicationsByMaster(masterId);
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(ApplicationMapper::toDTO)
                .toList();
        return ResponseEntity.ok(applicationDTOs);
    }
}