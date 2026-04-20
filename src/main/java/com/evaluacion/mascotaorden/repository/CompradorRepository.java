package com.evaluacion.mascotaorden.repository;

import com.evaluacion.mascotaorden.models.Comprador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompradorRepository extends JpaRepository<Comprador, Integer> {
    Optional<Comprador> findByEmailIgnoreCase(String email);
}
