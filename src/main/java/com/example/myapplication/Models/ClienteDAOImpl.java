package com.example.myapplication.Models;
import java.sql.Connection;
import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ClienteDAOImpl  implements ClienteDAO    {

    private SQLServerConnector sqlConnector;

    public ClienteDAOImpl(SQLServerConnector sqlConnector) {
        this.sqlConnector = sqlConnector;
    }

    @Override
    public boolean insertarCliente(Cliente cliente) {
        String query = "INSERT INTO Cliente (nombre, usuario, contrasena, correo, telefono, u_pedido) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = sqlConnector.connect();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getUsuario());
            stmt.setString(3, cliente.getContrasena());
            stmt.setString(4, cliente.getCorreo());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getuPedido());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Log.e("ClienteDAOImpl", "Error al insertar cliente", e);
            return false;
        } finally {
            sqlConnector.disconnect(conn); // Cerrar la conexión después de la operación
        }
    }

    @Override
    public boolean existeUsuario(String usuario) {
        String query = "SELECT COUNT(*) FROM Cliente WHERE usuario = ?";
        Connection conn = sqlConnector.connect();

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, usuario);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Si el conteo es mayor a 0, el usuario ya existe
            }
        } catch (SQLException e) {
            Log.e("ClienteDAOImpl", "Error al verificar existencia de usuario", e);
        } finally {
            sqlConnector.disconnect(conn); // Cerrar la conexión después de la operación
        }
        return false;
    }
    @Override
    public boolean existeCorreo(String correo) {
        String query = "SELECT COUNT(*) FROM Cliente WHERE correo = ?";
        Connection conn = sqlConnector.connect();

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, correo);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Si el conteo es mayor a 0, el correo ya existe
            }
        } catch (SQLException e) {
            Log.e("ClienteDAOImpl", "Error al verificar existencia de correo", e);
        } finally {
            sqlConnector.disconnect(conn); // Cerrar la conexión después de la operación
        }
        return false;
    }

    public boolean iniciarSesion(String usuario, String contrasena) {
        String query = "SELECT COUNT(*) FROM Cliente WHERE usuario = ? AND contrasena = ?";
        Connection conn = sqlConnector.connect();

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, usuario);
            preparedStatement.setString(2, contrasena);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Si el conteo es mayor a 0, las credenciales son correctas
            }
        } catch (SQLException e) {
            Log.e("ClienteDAOImpl", "Error al verificar credenciales de usuario", e);
        } finally {
            sqlConnector.disconnect(conn); // Cerrar la conexión después de la operación
        }
        return false; // Si ocurre algún error o no se encuentran coincidencias
    }


    @Override
    public Cliente obtenerClientePorUsuario(String usuario) {
        Cliente cliente = null;
        String sql = "SELECT * FROM Cliente WHERE usuario = ?";
        try (Connection conn = sqlConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cliente = new Cliente(
                        rs.getString("nombre"),
                        rs.getString("usuario"),
                        rs.getString("contrasena"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("u_pedido")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }


    @Override
    public boolean actualizarCliente(Cliente cliente) {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = "UPDATE Cliente SET nombre = ?, correo = ?, telefono = ? WHERE usuario = ?";

        try {
            // Obtener la conexión a la base de datos
            connection = sqlConnector.connect();

            // Preparar la sentencia SQL
            statement = connection.prepareStatement(query);
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getCorreo());
            statement.setString(3, cliente.getTelefono());
            statement.setString(4, cliente.getUsuario()); // Usamos el campo 'usuario' como identificador

            // Ejecutar la actualización
            int rowsAffected = statement.executeUpdate();

            // Retornar true si al menos una fila fue actualizada
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Cerrar recursos
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
