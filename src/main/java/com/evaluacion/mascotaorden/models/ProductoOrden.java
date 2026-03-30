package com.evaluacion.mascotaorden.models;

import java.math.BigDecimal;

public class ProductoOrden {

    private String nombre;
    private String categoria;
    private String sku;
    private BigDecimal precioUnitario;

    public ProductoOrden() {
    }

    public ProductoOrden(String nombre, String categoria, String sku, BigDecimal precioUnitario) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.sku = sku;
        this.precioUnitario = precioUnitario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
