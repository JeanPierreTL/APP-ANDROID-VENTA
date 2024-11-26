package com.example.myapplication.Models;

public class Pedido {
    private int idPedido;
    private int idCliente;
    private String nombreCliente;
    private String dniRuc;
    private String fechaPedido;
    private String estadoPedido;
    private double subtotal;
    private double total;
    private String modalidad;

    public Pedido() {

    }

    public Pedido(int idPedido, int idCliente, String nombreCliente, String dniRuc, String fechaPedido, String estadoPedido, double subtotal, double total, String modalidad) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.dniRuc = dniRuc;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.subtotal = subtotal;
        this.total = total;
        this.modalidad = modalidad;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDniRuc() {
        return dniRuc;
    }

    public void setDniRuc(String dniRuc) {
        this.dniRuc = dniRuc;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }
}
