package com.evaluacion.mascotaorden.repository;

import com.evaluacion.mascotaorden.models.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    List<OrdenCompra> findAllByOrderByIdAsc();
}
