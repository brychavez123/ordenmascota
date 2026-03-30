package com.evaluacion.mascotaorden.service;

import com.evaluacion.mascotaorden.models.Comprador;
import com.evaluacion.mascotaorden.models.EstadoOrden;
import com.evaluacion.mascotaorden.models.OrdenCompra;
import com.evaluacion.mascotaorden.models.ProductoOrden;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service

public class OrdenCompraServicio {

    // Lista en memoria que simula la tabla de ordenes.
    private final List<OrdenCompra> ordenes = new ArrayList<>();

    // Secuencia incremental para generar IDs unicos.
    private final AtomicLong secuenciaId = new AtomicLong(1);

    public OrdenCompraServicio() {
        // Orden de ejemplo 1
        ordenes.add(new OrdenCompra(
            secuenciaId.getAndIncrement(),
            new Comprador("Bryan", "bryan@mail.com", "+56911111111", "Av. Los Aromos 123"),
            "Luna",
            new ProductoOrden("Alimento Premium", "Alimentos", "ALIM-001", new BigDecimal("15990")),
            2,
            EstadoOrden.CREADA
        ));

        ordenes.add(new OrdenCompra(
            secuenciaId.getAndIncrement(),
            new Comprador("Camila", "camila@mail.com", "+56922222222", "Pasaje Norte 45"),
            "Thor",
            new ProductoOrden("Shampoo Hipoalergenico", "Higiene", "HIG-002", new BigDecimal("8990")),
            1,
            EstadoOrden.EN_PROCESO
        ));

        ordenes.add(new OrdenCompra(
            secuenciaId.getAndIncrement(),
            new Comprador("Matias", "matias@mail.com", "+56933333333", "Calle Sur 99"),
            "Milo",
            new ProductoOrden("Juguete Mordedor", "Juguetes", "JUG-003", new BigDecimal("4990")),
            3,
            EstadoOrden.ENTREGADA
        ));

        ordenes.add(new OrdenCompra(
            secuenciaId.getAndIncrement(),
            new Comprador("Valentina", "valentina@mail.com", "+56944444444", "Av. Central 88"),
            "Nina",
            new ProductoOrden("Arena Sanitaria", "Higiene", "HIG-004", new BigDecimal("6990")),
            2,
            EstadoOrden.CREADA
        ));

        ordenes.add(new OrdenCompra(
            secuenciaId.getAndIncrement(),
            new Comprador("Joaquin", "joaquin@mail.com", "+56955555555", "Los Pinos 7"),
            "Rocky",
            new ProductoOrden("Correa Reflectante", "Accesorios", "ACC-005", new BigDecimal("10990")),
            1,
            EstadoOrden.EN_PROCESO
        ));

        ordenes.add(new OrdenCompra(
            secuenciaId.getAndIncrement(),
            new Comprador("Fernanda", "fernanda@mail.com", "+56966666666", "Las Rosas 66"),
            "Coco",
            new ProductoOrden("Snacks Dentales", "Snacks", "SNK-006", new BigDecimal("2990")),
            4,
            EstadoOrden.CREADA
        ));

        ordenes.add(new OrdenCompra(
            secuenciaId.getAndIncrement(),
            new Comprador("Diego", "diego@mail.com", "+56977777777", "Santa Elena 12"),
            "Kira",
            new ProductoOrden("Cama Mediana", "Descanso", "HOG-007", new BigDecimal("24990")),
            1,
            EstadoOrden.CANCELADA
        ));

        ordenes.add(new OrdenCompra(
            secuenciaId.getAndIncrement(),
            new Comprador("Antonia", "antonia@mail.com", "+56988888888", "Prat 401"),
            "Simba",
            new ProductoOrden("Cepillo de Pelo", "Higiene", "HIG-008", new BigDecimal("5590")),
            2,
            EstadoOrden.ENTREGADA
        ));
    }

    /**
     * Retorna todas las ordenes disponibles en memoria.
     */
    public List<OrdenCompra> obtenerOrdenes() {
        return ordenes;
    }

    public Optional<OrdenCompra> obtenerOrdenPorId(Long id) {
        return ordenes.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }


    public List<String> validarDatos(String cliente, String email, String telefono, String direccion,
                                     String mascota, String producto, String categoriaProducto, String sku,
                                     Integer cantidad, BigDecimal precioUnitario) {
        List<String> errores = new ArrayList<>();

        if (cliente == null || cliente.isBlank()) {
            errores.add("El nombre del cliente es obligatorio");
        }
        if (email == null || email.isBlank()) {
            errores.add("El email del comprador es obligatorio");
        }
        if (telefono == null || telefono.isBlank()) {
            errores.add("El telefono del comprador es obligatorio");
        }
        if (direccion == null || direccion.isBlank()) {
            errores.add("La direccion del comprador es obligatoria");
        }
        if (mascota == null || mascota.isBlank()) {
            errores.add("El nombre de la mascota es obligatorio");
        }
        if (producto == null || producto.isBlank()) {
            errores.add("El nombre del producto es obligatorio");
        }
        if (categoriaProducto == null || categoriaProducto.isBlank()) {
            errores.add("La categoria del producto es obligatoria");
        }
        if (sku == null || sku.isBlank()) {
            errores.add("El SKU del producto es obligatorio");
        }
        if (cantidad == null || cantidad <= 0) {
            errores.add("La cantidad debe ser mayor a 0");
        }
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            errores.add("El precio unitario debe ser mayor a 0");
        }

        return errores;
    }

    /**
     * Crea una nueva orden con estado inicial CREADA y la agrega a la lista.
     */
    public OrdenCompra crearOrden(String cliente, String email, String telefono, String direccion,
                                  String mascota, String producto, String categoriaProducto, String sku,
                                  Integer cantidad, BigDecimal precioUnitario) {
        OrdenCompra nuevaOrden = new OrdenCompra(
                secuenciaId.getAndIncrement(),
                new Comprador(cliente.trim(), email.trim(), telefono.trim(), direccion.trim()),
                mascota.trim(),
                new ProductoOrden(producto.trim(), categoriaProducto.trim(), sku.trim(), precioUnitario),
                cantidad,
                EstadoOrden.CREADA
        );

        ordenes.add(nuevaOrden);
        return nuevaOrden;
    }

    public boolean cancelarOrden(OrdenCompra orden) {
        if (orden.getEstado() == EstadoOrden.ENTREGADA) {
            return false;
        }
        orden.setEstado(EstadoOrden.CANCELADA);
        return true;
    }
}