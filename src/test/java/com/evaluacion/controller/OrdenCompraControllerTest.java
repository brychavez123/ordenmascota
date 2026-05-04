package com.evaluacion.controller;

import com.evaluacion.mascotaorden.GlobalExceptionHandler;
import com.evaluacion.mascotaorden.controller.OrdenCompraController;
import com.evaluacion.mascotaorden.service.OrdenCompraServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrdenCompraControllerTest {

    private MockMvc mockMvc;
    private OrdenCompraServicio ordenCompraServicio;

    @BeforeEach
    void setUp() {
        ordenCompraServicio = mock(OrdenCompraServicio.class);
        OrdenCompraController controller = new OrdenCompraController(ordenCompraServicio);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listar_ok() throws Exception {
        when(ordenCompraServicio.listarOrdenes()).thenReturn(List.of());

        mockMvc.perform(get("/api/ordenes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(ordenCompraServicio).listarOrdenes();
    }

    @Test
    void crear_ok() throws Exception {
        when(ordenCompraServicio.crearOrden(
                "Bryan",
                "bryan@mail.com",
                "123456789",
                "Direccion 123",
                "Toby",
                "Collar",
                "Accesorio",
                "SKU-1",
                2,
                new BigDecimal("12000.50")
        )).thenReturn(Map.of("ok", true, "mensaje", "Orden creada correctamente."));

        mockMvc.perform(post("/api/ordenes")
                        .param("cliente", "Bryan")
                        .param("email", "bryan@mail.com")
                        .param("telefono", "123456789")
                        .param("direccion", "Direccion 123")
                        .param("mascota", "Toby")
                        .param("producto", "Collar")
                        .param("categoriaProducto", "Accesorio")
                        .param("sku", "SKU-1")
                        .param("cantidad", "2")
                        .param("precioUnitario", "12000.50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true));

        verify(ordenCompraServicio).crearOrden(
                "Bryan",
                "bryan@mail.com",
                "123456789",
                "Direccion 123",
                "Toby",
                "Collar",
                "Accesorio",
                "SKU-1",
                2,
                new BigDecimal("12000.50")
        );
    }

    @Test
    void obtener_noExiste() throws Exception {
        when(ordenCompraServicio.buscarOrdenPorId(999L))
                .thenReturn(Map.of("ok", false, "error", "No existe una orden con ese id"));

        mockMvc.perform(get("/api/ordenes/999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.error").value("No existe una orden con ese id"));

        verify(ordenCompraServicio).buscarOrdenPorId(999L);
    }

    @Test
    void crear_parametroFaltante() throws Exception {
        mockMvc.perform(post("/api/ordenes")
                        .param("cliente", "Bryan")
                        .param("email", "bryan@mail.com")
                        .param("telefono", "123456789")
                        .param("direccion", "Direccion 123")
                        .param("mascota", "Toby")
                        .param("categoriaProducto", "Accesorio")
                        .param("sku", "SKU-1")
                        .param("cantidad", "2")
                        .param("precioUnitario", "12000.50"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.error", containsString("Falta un parametro requerido")));

        verifyNoInteractions(ordenCompraServicio);
    }

    @Test
    void obtener_idInvalido() throws Exception {
        mockMvc.perform(get("/api/ordenes/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.error").value("Parametro invalido: revise id, cantidad, precioUnitario o estado"));

        verifyNoInteractions(ordenCompraServicio);
    }

    @Test
    void eliminar_noExiste() throws Exception {
        when(ordenCompraServicio.eliminarOrden(10L))
                .thenReturn(Map.of("ok", false, "error", "No existe una orden con ese id"));

        mockMvc.perform(delete("/api/ordenes/10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.error").value("No existe una orden con ese id"));

        verify(ordenCompraServicio).eliminarOrden(10L);
    }
}
