package com.evaluacion.mascotaorden.service;

import com.evaluacion.mascotaorden.models.Comprador;
import com.evaluacion.mascotaorden.models.EstadoOrden;
import com.evaluacion.mascotaorden.models.OrdenCompra;
import com.evaluacion.mascotaorden.models.ProductoOrden;
import com.evaluacion.mascotaorden.repository.CompradorRepository;
import com.evaluacion.mascotaorden.repository.OrdenCompraRepository;
import com.evaluacion.mascotaorden.repository.ProductoOrdenRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrdenCompraServicio {

    private final OrdenCompraRepository ordenCompraRepository;
    private final CompradorRepository compradorRepository;
    private final ProductoOrdenRepository productoOrdenRepository;

    public OrdenCompraServicio(
            OrdenCompraRepository ordenCompraRepository,
            CompradorRepository compradorRepository,
            ProductoOrdenRepository productoOrdenRepository
    ) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.compradorRepository = compradorRepository;
        this.productoOrdenRepository = productoOrdenRepository;
    }

    public List<OrdenCompra> listarOrdenes() {
        return ordenCompraRepository.findAllByOrderByIdAsc();
    }

    public Map<String, Object> buscarOrdenPorId(Long id) {
        OrdenCompra orden = ordenCompraRepository.findById(id).orElse(null);
        if (orden == null) {
            return Map.of("ok", false, "error", "No existe una orden con ese id");
        }
        return Map.of("ok", true, "data", orden);
    }

    public Map<String, Object> crearOrden(
            String cliente,
            String email,
            String telefono,
            String direccion,
            String mascota,
            String producto,
            String categoriaProducto,
            String sku,
            Integer cantidad,
            BigDecimal precioUnitario
    ) {
        List<String> errores = validarDatosCreacion(
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

        if (!errores.isEmpty()) {
            return Map.of("ok", false, "errores", errores);
        }

        String emailNormalizado = email.trim();
        Comprador comprador = compradorRepository.findByEmailIgnoreCase(emailNormalizado).orElse(null);
        if (comprador == null) {
            comprador = new Comprador(cliente.trim(), emailNormalizado, telefono.trim(), direccion.trim());
        } else {
            comprador.setNombre(cliente.trim());
            comprador.setTelefono(telefono.trim());
            comprador.setDireccion(direccion.trim());
        }
        comprador = compradorRepository.save(comprador);

        String skuNormalizado = sku.trim();
        ProductoOrden productoOrden = productoOrdenRepository.findBySkuIgnoreCase(skuNormalizado).orElse(null);
        if (productoOrden == null) {
            productoOrden = new ProductoOrden(producto.trim(), categoriaProducto.trim(), skuNormalizado, precioUnitario);
        } else {
            productoOrden.setNombre(producto.trim());
            productoOrden.setCategoria(categoriaProducto.trim());
            productoOrden.setPrecioUnitario(precioUnitario);
        }
        productoOrden = productoOrdenRepository.save(productoOrden);

        OrdenCompra nuevaOrden = new OrdenCompra(
                null,
                comprador,
                mascota.trim(),
                productoOrden,
                cantidad,
                EstadoOrden.CREADA
        );
        nuevaOrden.recalcularTotal();

        OrdenCompra guardada = ordenCompraRepository.save(nuevaOrden);

        return Map.of(
                "ok", true,
                "mensaje", "Orden creada correctamente.",
                "data", guardada
        );
    }

    public Map<String, Object> actualizarOrden(
            Long id,
            String email,
            String telefono,
            String mascota
    ) {
        OrdenCompra orden = ordenCompraRepository.findById(id).orElse(null);
        if (orden == null) {
            return Map.of("ok", false, "error", "No existe una orden con ese id");
        }

        boolean sinCambios = email == null && telefono == null && mascota == null;

        if (sinCambios) {
            return Map.of("ok", false, "error", "Debe enviar al menos un dato para actualizar");
        }

        Comprador compradorActual = orden.getComprador();
        boolean actualizarComprador = email != null || telefono != null;

        if (actualizarComprador) {
            if (compradorActual == null) {
                return Map.of("ok", false, "error", "La orden no tiene comprador asociado");
            }

            Comprador compradorFinal = compradorActual;

            if (email != null) {
                String emailFinal = email.trim();
                if (emailFinal.isBlank()) {
                    return Map.of("ok", false, "error", "El email del comprador es obligatorio");
                }

                Comprador compradorPorEmail = compradorRepository.findByEmailIgnoreCase(emailFinal).orElse(null);
                if (compradorPorEmail != null && !compradorPorEmail.getId().equals(compradorActual.getId())) {
                    compradorFinal = compradorPorEmail;
                    orden.setComprador(compradorFinal);
                }
                compradorFinal.setEmail(emailFinal);
            }

            if (telefono != null) {
                String telefonoFinal = telefono.trim();
                if (telefonoFinal.isBlank()) {
                    return Map.of("ok", false, "error", "El telefono del comprador es obligatorio");
                }
                compradorFinal.setTelefono(telefonoFinal);
            }

            compradorFinal = compradorRepository.save(compradorFinal);
            orden.setComprador(compradorFinal);
        }

        if (mascota != null) {
            String mascotaFinal = mascota.trim();
            if (mascotaFinal.isBlank()) {
                return Map.of("ok", false, "error", "El nombre de la mascota es obligatorio");
            }
            orden.setNombreMascota(mascotaFinal);
        }

        orden.recalcularTotal();

        OrdenCompra actualizada = ordenCompraRepository.save(orden);

        return Map.of(
                "ok", true,
                "mensaje", "Orden actualizada correctamente.",
                "data", actualizada
        );
    }

    public Map<String, Object> eliminarOrden(Long id) {
        if (!ordenCompraRepository.existsById(id)) {
            return Map.of("ok", false, "error", "No existe una orden con ese id");
        }

        ordenCompraRepository.deleteById(id);

        return Map.of(
                "ok", true,
                "mensaje", "Orden eliminada correctamente.",
                "id", id
        );
    }

    private List<String> validarDatosCreacion(
            String cliente,
            String email,
            String telefono,
            String direccion,
            String mascota,
            String producto,
            String categoriaProducto,
            String sku,
            Integer cantidad,
            BigDecimal precioUnitario
    ) {
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
}
