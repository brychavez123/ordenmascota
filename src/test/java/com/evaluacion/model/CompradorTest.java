package com.evaluacion.model;

import com.evaluacion.mascotaorden.models.Comprador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompradorTest {

    @Test
    void constructor_ok() {
        Comprador comprador = new Comprador(1, "Bryan", "bryan@mail.com", "987654321", "Av. Siempre Viva 123");

        assertEquals(1, comprador.getId());
        assertEquals("Bryan", comprador.getNombre());
        assertEquals("bryan@mail.com", comprador.getEmail());
        assertEquals("987654321", comprador.getTelefono());
        assertEquals("Av. Siempre Viva 123", comprador.getDireccion());
    }

    @Test
    void setters_ok() {
        Comprador comprador = new Comprador();

        comprador.setId(2);
        comprador.setNombre("Camila");
        comprador.setEmail("camila@mail.com");
        comprador.setTelefono("123456789");
        comprador.setDireccion("Pasaje 45");

        assertEquals(2, comprador.getId());
        assertEquals("Camila", comprador.getNombre());
        assertEquals("camila@mail.com", comprador.getEmail());
        assertEquals("123456789", comprador.getTelefono());
        assertEquals("Pasaje 45", comprador.getDireccion());
    }
}
