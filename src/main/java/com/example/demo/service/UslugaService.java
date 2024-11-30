package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Usluga;
import com.example.demo.repository.UslugaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UslugaService {
    @Autowired
    UslugaRepository uslugaRepository;

    public UslugaService() {
        super();
        // TODO Auto-generated constructor stub
    }


    public List<Usluga> getUslugas(Long id) {
        return (List<Usluga>) uslugaRepository.findByUserId(id);
    }

    public Optional<Usluga> findByID(Long id) {
        return uslugaRepository.findById(id);
    }
    public void saveUsluga(Usluga newUsluga) {
        uslugaRepository.save(newUsluga);
    }
    public void deleteUsluga(Long id) {
        Usluga usluga = uslugaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usluga", "id", id));
        uslugaRepository.delete(usluga);
    }
}
