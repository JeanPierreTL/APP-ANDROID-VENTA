package com.example.myapplication.Models;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class    SQLServerConnector {

    private static final String DRIVER = "net.sourceforge.jtds.jdbc.Driver";
    private static final String IP = "sql9001.site4now.net";
    private static final String PORT = "1433";
    private static final String DB = "db_aaec8e_paperosbd001";
    private static final String USERNAME = "db_aaec8e_paperosbd001_admin";
    private static final String PASSWORD = "@fabian456";

    public SQLServerConnector() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public Connection connect() {
        try {
            Class.forName(DRIVER);
            String connectionUrl = "jdbc:jtds:sqlserver://" + IP + ":" + PORT + ";databaseName=" + DB + ";encrypt=true;trustServerCertificate=true;";
            return DriverManager.getConnection(connectionUrl, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }

    public void disconnect(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error al cerrar la conexión con la base de datos", e);
            }
        }
    }
}


