package com.evaluacion.repository;

import com.evaluacion.mascotaorden.MascotaordenApplication;
import com.evaluacion.mascotaorden.models.ProductoOrden;
import com.evaluacion.mascotaorden.repository.ProductoOrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MascotaordenApplication.class)
@ActiveProfiles("test")
@Transactional
class ProductoOrdenRepositoryTest {

    @Autowired
    private ProductoOrdenRepository productoOrdenRepository;

    @BeforeEach
    void limpiar() {
        productoOrdenRepository.deleteAll();
    }

    @Test
    void buscarIgnoreCase_ok() {
        productoOrdenRepository.save(new ProductoOrden(null, "Collar", "Accesorio", "SKU-1", new BigDecimal("9990")));

        ProductoOrden resultado = productoOrdenRepository.findBySkuIgnoreCase("sku-1").orElse(null);

        assertTrue(resultado != null);
        assertEquals("Collar", resultado.getNombre());
    }

    @Test
    void buscar_noExiste() {
        boolean existe = productoOrdenRepository.findBySkuIgnoreCase("SKU-X").isPresent();

        assertEquals(false, existe);
    }
}
