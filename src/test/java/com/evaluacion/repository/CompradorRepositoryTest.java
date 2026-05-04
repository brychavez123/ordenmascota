package com.evaluacion.repository;

import com.evaluacion.mascotaorden.MascotaordenApplication;
import com.evaluacion.mascotaorden.models.Comprador;
import com.evaluacion.mascotaorden.repository.CompradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MascotaordenApplication.class)
@ActiveProfiles("test")
@Transactional
class CompradorRepositoryTest {

    @Autowired
    private CompradorRepository compradorRepository;

    @BeforeEach
    void limpiar() {
        compradorRepository.deleteAll();
    }

    @Test
    void buscarIgnoreCase_ok() {
        compradorRepository.save(new Comprador(null, "Bryan", "Bryan@Mail.com", "9999", "Direccion"));

        Comprador resultado = compradorRepository.findByEmailIgnoreCase("bryan@mail.com").orElse(null);

        assertTrue(resultado != null);
        assertEquals("Bryan", resultado.getNombre());
    }

    @Test
    void buscar_noExiste() {
        boolean existe = compradorRepository.findByEmailIgnoreCase("nadie@mail.com").isPresent();

        assertEquals(false, existe);
    }
}
