package com.evaluacion.mascotaorden.models;

import java.math.BigDecimal;

public class OrdenCompra {

    private Long id;
    private Comprador comprador;
    private String nombreMascota;
    private ProductoOrden producto;
    private Integer cantidad;
    private BigDecimal total;
    private EstadoOrden estado;

    public OrdenCompra() {
    }

    public OrdenCompra(Long id, Comprador comprador, String nombreMascota, ProductoOrden producto,
                       Integer cantidad, EstadoOrden estado) {
        this.id = id;
        this.comprador = comprador;
        this.nombreMascota = nombreMascota;
        this.producto = producto;
        this.cantidad = cantidad;
        this.total = producto.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad));
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public ProductoOrden getProducto() {
        return producto;
    }

    public void setProducto(ProductoOrden producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    // Getters de compatibilidad para mantener respuestas simples en el JSON.
    public String getCliente() {
        return comprador != null ? comprador.getNombre() : null;
    }

    public String getMascota() {
        return nombreMascota;
    }

    public String getProductoNombre() {
        return producto != null ? producto.getNombre() : null;
    }

    public String getProductoCategoria() {
        return producto != null ? producto.getCategoria() : null;
    }

    public String getProductoSku() {
        return producto != null ? producto.getSku() : null;
    }

    public BigDecimal getPrecioUnitario() {
        return producto != null ? producto.getPrecioUnitario() : null;
    }
}