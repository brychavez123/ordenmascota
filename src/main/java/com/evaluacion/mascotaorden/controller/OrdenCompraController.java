package com.evaluacion.mascotaorden.controller;

import com.evaluacion.mascotaorden.service.OrdenCompraServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenCompraController {

    private final OrdenCompraServicio ordenCompraServicio;

    public OrdenCompraController(OrdenCompraServicio ordenCompraServicio) {
        this.ordenCompraServicio = ordenCompraServicio;
    }

    @GetMapping
    public ResponseEntity<?> listarOrdenes() {
        return ResponseEntity.ok(ordenCompraServicio.listarOrdenes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerOrdenPorId(@PathVariable Long id) {
        Map<String, Object> resultado = ordenCompraServicio.buscarOrdenPorId(id);
        if (Boolean.FALSE.equals(resultado.get("ok"))) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<?> crearOrden(
            @RequestParam String cliente,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam String direccion,
            @RequestParam String mascota,
            @RequestParam String producto,
            @RequestParam String categoriaProducto,
            @RequestParam String sku,
            @RequestParam Integer cantidad,
            @RequestParam BigDecimal precioUnitario
    ) {
        Map<String, Object> resultado = ordenCompraServicio.crearOrden(
                cliente,
                email,
                telefono,
                direccion,
                mascota,
                producto,
                categoriaProducto,
                sku,
                cantidad,
                precioUnitario
        );

        if (Boolean.FALSE.equals(resultado.get("ok"))) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOrden(
            @PathVariable Long id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String mascota
    ) {
        Map<String, Object> resultado = ordenCompraServicio.actualizarOrden(
                id,
                email,
                telefono,
                mascota
        );

        if (Boolean.FALSE.equals(resultado.get("ok"))) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarOrden(@PathVariable Long id) {
        Map<String, Object> resultado = ordenCompraServicio.eliminarOrden(id);
        if (Boolean.FALSE.equals(resultado.get("ok"))) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }
}
