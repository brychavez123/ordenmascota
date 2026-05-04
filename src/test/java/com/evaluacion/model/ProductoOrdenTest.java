package com.evaluacion.model;

import com.evaluacion.mascotaorden.models.ProductoOrden;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductoOrdenTest {

    @Test
    void constructor_ok() {
        ProductoOrden producto = new ProductoOrden(1, "Alimento Premium", "Comida", "SKU-100", new BigDecimal("14990"));

        assertEquals(1, producto.getId());
        assertEquals("Alimento Premium", producto.getNombre());
        assertEquals("Comida", producto.getCategoria());
        assertEquals("SKU-100", producto.getSku());
        assertEquals(new BigDecimal("14990"), producto.getPrecioUnitario());
    }

    @Test
    void setters_ok() {
        ProductoOrden producto = new ProductoOrden();

        producto.setId(2);
        producto.setNombre("Collar");
        producto.setCategoria("Accesorio");
        producto.setSku("SKU-200");
        producto.setPrecioUnitario(new BigDecimal("8990"));

        assertEquals(2, producto.getId());
        assertEquals("Collar", producto.getNombre());
        assertEquals("Accesorio", producto.getCategoria());
        assertEquals("SKU-200", producto.getSku());
        assertEquals(new BigDecimal("8990"), producto.getPrecioUnitario());
    }
}
