package com.evaluacion.repository;

import com.evaluacion.mascotaorden.MascotaordenApplication;
import com.evaluacion.mascotaorden.models.Comprador;
import com.evaluacion.mascotaorden.models.EstadoOrden;
import com.evaluacion.mascotaorden.models.OrdenCompra;
import com.evaluacion.mascotaorden.models.ProductoOrden;
import com.evaluacion.mascotaorden.repository.CompradorRepository;
import com.evaluacion.mascotaorden.repository.OrdenCompraRepository;
import com.evaluacion.mascotaorden.repository.ProductoOrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MascotaordenApplication.class)
@ActiveProfiles("test")
@Transactional
class OrdenCompraRepositoryTest {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private CompradorRepository compradorRepository;

    @Autowired
    private ProductoOrdenRepository productoOrdenRepository;

    @BeforeEach
    void limpiar() {
        ordenCompraRepository.deleteAll();
        compradorRepository.deleteAll();
        productoOrdenRepository.deleteAll();
    }

    @Test
    void listarOrdenado_ok() {
        OrdenCompra primera = crearOrden("a@mail.com", "SKU-A", "Toby", 1);
        OrdenCompra segunda = crearOrden("b@mail.com", "SKU-B", "Luna", 2);

        List<OrdenCompra> ordenes = ordenCompraRepository.findAllByOrderByIdAsc();

        assertEquals(2, ordenes.size());
        assertEquals(primera.getId(), ordenes.get(0).getId());
        assertEquals(segunda.getId(), ordenes.get(1).getId());
        assertTrue(ordenes.get(0).getId() < ordenes.get(1).getId());
    }

    @Test
    void buscarPorId_ok() {
        OrdenCompra creada = crearOrden("c@mail.com", "SKU-C", "Firulais", 3);

        OrdenCompra encontrada = ordenCompraRepository.findById(creada.getId()).orElse(null);

        assertTrue(encontrada != null);
        assertEquals("Firulais", encontrada.getNombreMascota());
        assertEquals(3, encontrada.getCantidad());
    }

    private OrdenCompra crearOrden(String email, String sku, String mascota, Integer cantidad) {
        Comprador comprador = compradorRepository.save(
                new Comprador(null, "Cliente " + sku, email, "11111111", "Direccion")
        );

        ProductoOrden producto = productoOrdenRepository.save(
                new ProductoOrden(null, "Producto " + sku, "Categoria", sku, new BigDecimal("5000"))
        );

        OrdenCompra orden = new OrdenCompra(null, comprador, mascota, producto, cantidad, EstadoOrden.CREADA);
        return ordenCompraRepository.save(orden);
    }
}
