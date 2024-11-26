package com.example.myapplication.Models;
import android.util.Log;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class PedidoDAOImpl  implements  PedidoDAO{


    private SQLServerConnector sqlConnector;

    public PedidoDAOImpl(SQLServerConnector sqlConnector) {
        this.sqlConnector = sqlConnector;
    }


    public int insertarPedido(Pedido pedido) {
        String sql = "INSERT INTO Pedido (id_cliente, fecha_pedido, estado_pedido, subtotal, total, modlidad,nombre_cliente, dni_ruc) " +
                "OUTPUT INSERTED.id_pedido VALUES (?, GETDATE(), ?, ?, ?, ?, ?,?)";
        try (Connection connection = sqlConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, pedido.getIdCliente());
            preparedStatement.setString(2, pedido.getEstadoPedido());
            preparedStatement.setDouble(3, pedido.getSubtotal());
            preparedStatement.setDouble(4, pedido.getTotal());
            preparedStatement.setString(5, pedido.getModalidad());
            preparedStatement.setString(6, pedido.getNombreCliente());
            preparedStatement.setString(7, pedido.getDniRuc());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            Log.e("PedidoDAOImpl", "Error al insertar Pedido", e);
        }
        return -1; // Error
    }


    public void insertarDetallePedido(DetallePedido detallePedido) {
        String sql = "INSERT INTO DetallePedido (id_pedido, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = sqlConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, detallePedido.getIdPedido());
            preparedStatement.setInt(2, detallePedido.getIdProducto());
            preparedStatement.setInt(3, detallePedido.getCantidad());
            preparedStatement.setDouble(4, detallePedido.getPrecioUnitario());
            preparedStatement.setDouble(5, detallePedido.getSubtotal());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            Log.e("PedidoDAOImpl", "Error al insertar DetallePedido", e);
        }
    }

    public void insertarPago(Pago pago) {
        String sql = "INSERT INTO Pago (id_pedido, monto, metodo_pago, fecha_pago, estado_pago) VALUES (?, ?, ?, GETDATE(), ?)";
        try (Connection connection = sqlConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, pago.getIdPedido());
            preparedStatement.setDouble(2, pago.getMonto());
            preparedStatement.setString(3, pago.getMetodoPago());
            preparedStatement.setString(4, pago.getEstadoPago());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            Log.e("PedidoDAOImpl", "Error al insertar Pago", e);
        }
    }
}



















