package com.evaluacion.service;

import com.evaluacion.mascotaorden.models.Comprador;
import com.evaluacion.mascotaorden.models.EstadoOrden;
import com.evaluacion.mascotaorden.models.OrdenCompra;
import com.evaluacion.mascotaorden.models.ProductoOrden;
import com.evaluacion.mascotaorden.repository.CompradorRepository;
import com.evaluacion.mascotaorden.repository.OrdenCompraRepository;
import com.evaluacion.mascotaorden.repository.ProductoOrdenRepository;
import com.evaluacion.mascotaorden.service.OrdenCompraServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class OrdenCompraServicioTest {

    private OrdenCompraRepository ordenCompraRepository;
    private CompradorRepository compradorRepository;
    private ProductoOrdenRepository productoOrdenRepository;
    private OrdenCompraServicio service;

    @BeforeEach
    void setUp() {
        ordenCompraRepository = mock(OrdenCompraRepository.class);
        compradorRepository = mock(CompradorRepository.class);
        productoOrdenRepository = mock(ProductoOrdenRepository.class);

        service = new OrdenCompraServicio(
                ordenCompraRepository,
                compradorRepository,
                productoOrdenRepository
        );
    }

    @Test
    void listar_ok() {
        List<OrdenCompra> esperado = List.of(new OrdenCompra());
        when(ordenCompraRepository.findAllByOrderByIdAsc()).thenReturn(esperado);

        List<OrdenCompra> resultado = service.listarOrdenes();

        assertEquals(esperado, resultado);
    }

    @Test
    void buscar_noExiste() {
        when(ordenCompraRepository.findById(1L)).thenReturn(Optional.empty());

        Map<String, Object> resultado = service.buscarOrdenPorId(1L);

        assertEquals(false, resultado.get("ok"));
        assertEquals("No existe una orden con ese id", resultado.get("error"));
    }

    @Test
    void crear_ok() {
        Comprador compradorGuardado = new Comprador(1, "Bryan", "bryan@mail.com", "9999", "Direccion 1");
        ProductoOrden productoGuardado = new ProductoOrden(2, "Collar", "Accesorio", "SKU-1", new BigDecimal("10000"));

        when(compradorRepository.findByEmailIgnoreCase("bryan@mail.com")).thenReturn(Optional.empty());
        when(compradorRepository.save(any(Comprador.class))).thenReturn(compradorGuardado);

        when(productoOrdenRepository.findBySkuIgnoreCase("SKU-1")).thenReturn(Optional.empty());
        when(productoOrdenRepository.save(any(ProductoOrden.class))).thenReturn(productoGuardado);

        when(ordenCompraRepository.save(any(OrdenCompra.class))).thenAnswer(invocation -> {
            OrdenCompra orden = invocation.getArgument(0);
            orden.setId(10L);
            return orden;
        });

        Map<String, Object> resultado = service.crearOrden(
                "Bryan",
                "bryan@mail.com",
                "9999",
                "Direccion 1",
                "Toby",
                "Collar",
                "Accesorio",
                "SKU-1",
                2,
                new BigDecimal("10000")
        );

        assertEquals(true, resultado.get("ok"));
        assertEquals("Orden creada correctamente.", resultado.get("mensaje"));

        ArgumentCaptor<OrdenCompra> captor = ArgumentCaptor.forClass(OrdenCompra.class);
        verify(ordenCompraRepository).save(captor.capture());

        OrdenCompra guardada = captor.getValue();
        assertEquals("Toby", guardada.getNombreMascota());
        assertEquals(2, guardada.getCantidad());
        assertEquals(EstadoOrden.CREADA, guardada.getEstado());
        assertEquals(0, guardada.getTotal().compareTo(new BigDecimal("20000")));
    }

    @Test
    void crear_datosInvalidos() {
        Map<String, Object> resultado = service.crearOrden(
                " ",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                BigDecimal.ZERO
        );

        assertEquals(false, resultado.get("ok"));
        List<?> errores = (List<?>) resultado.get("errores");
        assertTrue(errores.contains("El nombre del cliente es obligatorio"));
        assertTrue(errores.contains("La cantidad debe ser mayor a 0"));

        verifyNoInteractions(ordenCompraRepository, compradorRepository, productoOrdenRepository);
    }

    @Test
    void actualizar_noExiste() {
        when(ordenCompraRepository.findById(99L)).thenReturn(Optional.empty());

        Map<String, Object> resultado = service.actualizarOrden(99L, "nuevo@mail.com", "123", "Luna");

        assertEquals(false, resultado.get("ok"));
        assertEquals("No existe una orden con ese id", resultado.get("error"));
    }

    @Test
    void actualizar_sinCambios() {
        Comprador comprador = new Comprador(1, "Bryan", "bryan@mail.com", "9999", "Direccion 1");
        ProductoOrden producto = new ProductoOrden(1, "Collar", "Accesorio", "SKU-1", new BigDecimal("10000"));
        OrdenCompra orden = new OrdenCompra(1L, comprador, "Toby", producto, 1, EstadoOrden.CREADA);

        when(ordenCompraRepository.findById(1L)).thenReturn(Optional.of(orden));

        Map<String, Object> resultado = service.actualizarOrden(1L, null, null, null);

        assertEquals(false, resultado.get("ok"));
        assertEquals("Debe enviar al menos un dato para actualizar", resultado.get("error"));
    }

    @Test
    void actualizar_ok() {
        Comprador comprador = new Comprador(1, "Bryan", "bryan@mail.com", "9999", "Direccion 1");
        ProductoOrden producto = new ProductoOrden(1, "Collar", "Accesorio", "SKU-1", new BigDecimal("10000"));
        OrdenCompra orden = new OrdenCompra(1L, comprador, "Toby", producto, 1, EstadoOrden.CREADA);

        when(ordenCompraRepository.findById(1L)).thenReturn(Optional.of(orden));
        when(compradorRepository.findByEmailIgnoreCase("nuevo@mail.com")).thenReturn(Optional.empty());
        when(compradorRepository.save(any(Comprador.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ordenCompraRepository.save(any(OrdenCompra.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> resultado = service.actualizarOrden(1L, "nuevo@mail.com", "123456789", "Luna");

        assertEquals(true, resultado.get("ok"));
        assertEquals("Luna", orden.getNombreMascota());
        assertEquals("nuevo@mail.com", orden.getComprador().getEmail());
        assertEquals("123456789", orden.getComprador().getTelefono());
    }

    @Test
    void eliminar_ok() {
        when(ordenCompraRepository.existsById(5L)).thenReturn(true);

        Map<String, Object> resultado = service.eliminarOrden(5L);

        assertEquals(true, resultado.get("ok"));
        assertEquals(5L, resultado.get("id"));
        verify(ordenCompraRepository).deleteById(5L);
    }

    @Test
    void eliminar_noExiste() {
        when(ordenCompraRepository.existsById(5L)).thenReturn(false);

        Map<String, Object> resultado = service.eliminarOrden(5L);

        assertEquals(false, resultado.get("ok"));
        assertEquals("No existe una orden con ese id", resultado.get("error"));
    }
}
