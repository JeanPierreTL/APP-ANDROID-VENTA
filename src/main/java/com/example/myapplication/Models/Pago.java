package com.example.myapplication.Models;

public class Pago {
    private int idPago;
    private int idPedido;
    private double monto;
    private String metodoPago;
    private String fechaPago; // Se puede manejar como String o LocalDate dependiendo de tu preferencia
    private String estadoPago;


    public Pago(int idPago, int idPedido, double monto, String metodoPago, String fechaPago, String estadoPago) {
        this.idPago = idPago;
        this.idPedido = idPedido;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fechaPago = fechaPago;
        this.estadoPago = estadoPago;
    }

    public Pago() {
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }
}
