package com.example.demo.controller;

import com.example.demo.model.Application;
import com.example.demo.model.Slot;
import com.example.demo.model.User;
import com.example.demo.model.Usluga;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.SlotRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UslugaRepository;
import com.example.demo.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final UslugaRepository uslugaRepository;

    private final SlotRepository slotRepository;

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationRepository applicationRepository, UserRepository userRepository, UslugaRepository uslugaRepository, SlotRepository slotRepository, ApplicationService applicationService) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.uslugaRepository = uslugaRepository;
        this.slotRepository = slotRepository;
        this.applicationService = applicationService;
    }

    @Operation(summary = "Запись на услугу")
    @PostMapping("/{userId}/{slotId}")
    public ResponseEntity<?> applyForUsluga(@PathVariable Long userId, @PathVariable Long slotId, @RequestBody Application application) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Slot> optionalSlot = slotRepository.findById(slotId);
        //Optional<Usluga> optionalUsluga = uslugaRepository.findById(uslugaId);

        if (optionalUser.isPresent() && optionalSlot.isPresent()) {
            Slot slot = optionalSlot.get();
            if (!slot.isAvailable()) {
                return ResponseEntity.badRequest().body("Этот слот уже занят.");
            }

            User user = optionalUser.get();
            Usluga usluga = slot.getUsluga();

            application.setUsluga(slot.getUsluga());
            application.setUslugaName(usluga.getName());
            application.setUser(user);
            application.setApplicantName(user.getName());
            application.setApplicantEmail(user.getEmail());
            application.setDate(slot.getDate());
            application.setTime(slot.getTime());
            application.setSlot(slot);

            slot.setAvailable(false);
            slotRepository.save(slot);

            Application savedApplication = applicationRepository.save(application);
            return ResponseEntity.ok(savedApplication);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(summary = "Посмотреть записи на определенную услугу")
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @GetMapping("/usluga/{uslugaId}")
    public ResponseEntity<List<Application>> getApplicationsForUsluga(@PathVariable Long uslugaId) {
        List<Application> applications = applicationService.getApplicationsUsluga(uslugaId);
        return ResponseEntity.ok(applications);
    }

    @Operation(summary = "Посмотреть записи к определенному мастеру")
    @GetMapping("/master/{userId}")
    public ResponseEntity<List<Application>> getApplicationsForUser(@PathVariable Long userId) {
        List<Application> applications = applicationService.getApplicationsUser(userId);
        return ResponseEntity.ok(applications);
    }

    /*@Operation(summary = "Удалить запись")
    @DeleteMapping("/{id}")
    @Transactional // Гарантируем выполнение операций в рамках одной транзакции
    public ResponseEntity<?> withdrawApplication(@PathVariable(value = "id") long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);


        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            Usluga usluga = application.getUsluga();
            List<Slot> slots = usluga.getSlots(); // Пcолучаем список слотов, а не один слот

            // Найдем нужный слот, который относится к данной записи (нужна дополнительная логика, если это не так)
            Slot slot = slots.stream().filter(s -> s.getId().equals(application.getSlot().getId())).findFirst().orElse(null);
            if (slot == null) {
                return ResponseEntity.badRequest().body("No slot associated with this application.");
            }

            slot.setAvailable(true); // Установка слота как доступного
            slotRepository.save(slot); // Сохранение изменений слота

            applicationRepository.delete(application); // Удаление записи
            return ResponseEntity.ok("Application withdrawn and slot made available");
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @Operation(summary = "Удалить запись")
    @DeleteMapping("/{id}")
    @Transactional // Гарантируем выполнение операций в рамках одной транзакции
    public ResponseEntity<?> withdrawApplication(@PathVariable(value = "id") long id) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);

        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            Slot slot = application.getSlot(); // Получаем слот напрямую из записи

            if (slot != null) {
                // Разрываем связь между Slot и Application
                slot.setApplication(null);
                slot.setAvailable(true); // Устанавливаем слот как доступный
                slotRepository.save(slot); // Сохраняем изменения слота
            }

            // Удаляем запись
            applicationRepository.delete(application);
            return ResponseEntity.ok("Application withdrawn and slot made available");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}