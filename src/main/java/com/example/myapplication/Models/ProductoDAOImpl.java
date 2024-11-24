package com.example.myapplication.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    private SQLServerConnector sqlConnector; // Clase que gestiona la conexión

    public ProductoDAOImpl( SQLServerConnector sqlConnector) {
        this.sqlConnector = sqlConnector;
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        String query = "SELECT * FROM Producto"; // Consulta SQL

        try (Connection conn = sqlConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Crear un objeto Producto con los datos de la base de datos
                Producto producto = new Producto(
                        rs.getInt("id_producto"),
                        rs.getString("nombre_producto"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getBoolean("disponibilidad"),
                        rs.getInt("stock"),
                        rs.getString("imagen")
                );

                productos.add(producto); // Agregar producto a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }
}
