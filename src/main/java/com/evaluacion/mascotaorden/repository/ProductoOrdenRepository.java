package com.evaluacion.mascotaorden.repository;

import com.evaluacion.mascotaorden.models.ProductoOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoOrdenRepository extends JpaRepository<ProductoOrden, Integer> {
    Optional<ProductoOrden> findBySkuIgnoreCase(String sku);
}
