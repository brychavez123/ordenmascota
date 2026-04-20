package com.evaluacion.mascotaorden.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ordenmascota_producto_orden")
public class ProductoOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NOMBRE", nullable = false, length = 120)
    private String nombre;

    @Column(name = "CATEGORIA", nullable = false, length = 80)
    private String categoria;

    @Column(name = "SKU", nullable = false, length = 60, unique = true)
    private String sku;

    @Column(name = "PRECIO_UNITARIO", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    public ProductoOrden() {
    }

    public ProductoOrden(Integer id, String nombre, String categoria, String sku, BigDecimal precioUnitario) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.sku = sku;
        this.precioUnitario = precioUnitario;
    }

    public ProductoOrden(String nombre, String categoria, String sku, BigDecimal precioUnitario) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.sku = sku;
        this.precioUnitario = precioUnitario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
