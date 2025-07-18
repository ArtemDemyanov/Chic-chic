package com.example.demo.repository;

import com.example.demo.model.ModerationStatus;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.model.Usluga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UslugaRepository extends JpaRepository<Usluga, Long> {

    List<Usluga> findByUserId(Long userId);

    // только одобренные услуги мастера
    List<Usluga> findByUserIdAndStatus(Long userId, ModerationStatus status);


    List<Usluga> findByNameContainingIgnoreCase(String name);

    List<Usluga> findByLocation(String location);

    // для публичных запросов — только одобренные
    List<Usluga> findByStatus(ModerationStatus status);
}