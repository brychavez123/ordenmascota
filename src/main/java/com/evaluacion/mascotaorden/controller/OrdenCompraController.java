package com.evaluacion.mascotaorden.controller;

import com.evaluacion.mascotaorden.models.OrdenCompra;
import com.evaluacion.mascotaorden.service.OrdenCompraServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenCompraController {

    private final OrdenCompraServicio ordenCompraServicio;

    public OrdenCompraController(OrdenCompraServicio ordenCompraServicio) {
        this.ordenCompraServicio = ordenCompraServicio;
    }

    @GetMapping
    public List<OrdenCompra> obtenerOrdenes() {
        return ordenCompraServicio.obtenerOrdenes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerOrdenPorId(@PathVariable Long id) {
        Optional<OrdenCompra> orden = ordenCompraServicio.obtenerOrdenPorId(id);
        if (orden.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe una orden con id " + id));
        }
        return ResponseEntity.ok(orden.get());
    }

    @GetMapping("/estado/{id}")
    public ResponseEntity<?> obtenerEstadoOrden(@PathVariable Long id) {
        Optional<OrdenCompra> orden = ordenCompraServicio.obtenerOrdenPorId(id);
        if (orden.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe una orden con id " + id));
        }

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", orden.get().getId());
        respuesta.put("estado", orden.get().getEstado());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/crear")
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
            @RequestParam BigDecimal precioUnitario) {

        List<String> errores = ordenCompraServicio.validarDatos(
                cliente, email, telefono, direccion, mascota, producto, categoriaProducto, sku, cantidad, precioUnitario
        );
        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errores", errores));
        }

        OrdenCompra nuevaOrden = ordenCompraServicio.crearOrden(
                cliente, email, telefono, direccion, mascota, producto, categoriaProducto, sku, cantidad, precioUnitario
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOrden);
    }

    @GetMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarOrden(@PathVariable Long id) {
        Optional<OrdenCompra> orden = ordenCompraServicio.obtenerOrdenPorId(id);
        if (orden.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No existe una orden con id " + id));
        }

        boolean seCancelo = ordenCompraServicio.cancelarOrden(orden.get());
        if (!seCancelo) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "No se puede cancelar una orden que ya fue entregada"));
        }

        return ResponseEntity.ok(orden.get());
    }
}