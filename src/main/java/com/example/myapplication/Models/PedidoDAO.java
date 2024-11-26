package com.example.myapplication.Models;

public interface PedidoDAO {
    int insertarPedido(Pedido pedido);
    void insertarDetallePedido(DetallePedido detallePedido);
    void insertarPago(Pago pago);

}
