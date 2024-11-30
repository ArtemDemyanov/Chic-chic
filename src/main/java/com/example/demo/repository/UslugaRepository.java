package com.example.demo.repository;

import com.example.demo.model.Usluga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UslugaRepository extends JpaRepository<Usluga, Long> {
    List<Usluga> findByUserId(Long userId);
    List<Usluga> findByNameContainingIgnoreCase(String name);
    List<Usluga> findByLocation(String location);

}