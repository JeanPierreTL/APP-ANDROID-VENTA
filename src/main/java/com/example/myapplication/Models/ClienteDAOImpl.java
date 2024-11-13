package com.example.myapplication.Models;
import java.sql.Connection;
import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ClienteDAOImpl  implements ClienteDAO    {

    private SQLServerConnector sqlConnector;

    public ClienteDAOImpl(SQLServerConnector sqlConnector) {
        this.sqlConnector = sqlConnector;
    }
    @Override
    public boolean insertarCliente(Cliente cliente) {

        Connection conn = sqlConnector.connect();
       PreparedStatement stmt = null;
        try {
            if (conn != null) {
                String query = "INSERT INTO Cliente (nombre, usuario, contrasena, correo, telefono, u_pedido) VALUES (?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, cliente.getNombre());
                stmt.setString(2, cliente.getUsuario());
                stmt.setString(3, cliente.getContrasena());
                stmt.setString(4, cliente.getCorreo());
                stmt.setString(5, cliente.getTelefono());
                stmt.setString(6, cliente.getuPedido());
                stmt.executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Log.e("ClienteDAOImpl", "Error al insertar cliente", e);
            return false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    Log.e("ClienteDAOImpl", "Error al cerrar PreparedStatement", e);
                }
            }
            sqlConnector.disconnect();
        }
    }
}
