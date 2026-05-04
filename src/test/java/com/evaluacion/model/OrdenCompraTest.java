package com.evaluacion.model;

import com.evaluacion.mascotaorden.models.Comprador;
import com.evaluacion.mascotaorden.models.EstadoOrden;
import com.evaluacion.mascotaorden.models.OrdenCompra;
import com.evaluacion.mascotaorden.models.ProductoOrden;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrdenCompraTest {

    @Test
    void constructor_ok() {
        Comprador comprador = new Comprador(3, "Bryan", "bryan@mail.com", "9999", "Direccion 1");
        ProductoOrden producto = new ProductoOrden(5, "Arena", "Higiene", "SKU-500", new BigDecimal("7000"));

        OrdenCompra orden = new OrdenCompra(10L, comprador, "Toby", producto, 2, EstadoOrden.CREADA);

        assertEquals(10L, orden.getId());
        assertEquals(comprador, orden.getComprador());
        assertEquals(producto, orden.getProducto());
        assertEquals("Toby", orden.getNombreMascota());
        assertEquals(2, orden.getCantidad());
        assertEquals(EstadoOrden.CREADA, orden.getEstado());
        assertEquals(0, orden.getTotal().compareTo(new BigDecimal("14000")));
        assertEquals("Bryan", orden.getCliente());
        assertEquals("Toby", orden.getMascota());
        assertEquals("Arena", orden.getProductoNombre());
        assertEquals("Higiene", orden.getProductoCategoria());
        assertEquals("SKU-500", orden.getProductoSku());
        assertEquals(0, orden.getPrecioUnitario().compareTo(new BigDecimal("7000")));
        assertEquals(3, orden.getCompradorId());
        assertEquals(5, orden.getProductoId());
    }

    @Test
    void constructor_conNulls_ok() {
        OrdenCompra orden = new OrdenCompra(11L, null, "Luna", null, 1, EstadoOrden.CREADA);

        assertNull(orden.getCliente());
        assertNull(orden.getProductoNombre());
        assertNull(orden.getProductoCategoria());
        assertNull(orden.getProductoSku());
        assertNull(orden.getPrecioUnitario());
        assertNull(orden.getCompradorId());
        assertNull(orden.getProductoId());
        assertNull(orden.getTotal());
    }

    @Test
    void recalcularTotal_ok() {
        OrdenCompra orden = new OrdenCompra();
        ProductoOrden producto = new ProductoOrden(8, "Juguete", "Accesorio", "SKU-800", new BigDecimal("2500"));

        orden.setProducto(producto);
        orden.setCantidad(4);
        orden.recalcularTotal();

        assertEquals(0, orden.getTotal().compareTo(new BigDecimal("10000")));
    }
}
