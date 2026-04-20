package com.evaluacion.mascotaorden.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;

@Entity
@Table(name = "ordenmascota_orden_compra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPRADOR_ID", nullable = false)
    private Comprador comprador;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCTO_ID", nullable = false)
    private ProductoOrden producto;

    @Column(name = "NOMBRE_MASCOTA", nullable = false, length = 80)
    private String nombreMascota;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "TOTAL", nullable = false, precision = 14, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 20)
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
        this.estado = estado;
        recalcularTotal();
    }

    @PrePersist
    @PreUpdate
    public void recalcularTotal() {
        if (producto != null && producto.getPrecioUnitario() != null && cantidad != null) {
            this.total = producto.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad));
        }
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

    @Transient
    public Integer getCompradorId() {
        return comprador != null ? comprador.getId() : null;
    }

    @Transient
    public Integer getProductoId() {
        return producto != null ? producto.getId() : null;
    }
}
