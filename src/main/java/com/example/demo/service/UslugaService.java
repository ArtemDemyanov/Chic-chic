package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ModerationStatus;
import com.example.demo.model.User;
import com.example.demo.model.Usluga;
import com.example.demo.repository.UslugaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UslugaService {

    @Autowired
    UslugaRepository uslugaRepository;

    public UslugaService() {
        super();
    }

    public List<Usluga> getUslugas(Long id) {
        return (List<Usluga>) uslugaRepository.findByUserId(id);
    }

    // оставил этот метод на будущее, на всякий случай
    public List<Usluga> getServicesForViewer(Long userId, Long requesterId, boolean isAdmin) {
        if (isAdmin || Objects.equals(userId, requesterId)) {
            return uslugaRepository.findByUserId(userId);
        } else {
            return uslugaRepository.findByUserIdAndStatus(userId, ModerationStatus.APPROVED);
        }
    }

    public Optional<Usluga> findByID(Long id) {
        return uslugaRepository.findById(id);
    }

    public Usluga saveUsluga(Usluga newUsluga) {
        return uslugaRepository.save(newUsluga);
    }

    public void deleteUsluga(Long id) {
        Usluga usluga = uslugaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usluga", "id", id));
        uslugaRepository.delete(usluga);
    }

    public List<Usluga> findAllUsluga() {
        return uslugaRepository.findByStatus(ModerationStatus.APPROVED);
    }
}
