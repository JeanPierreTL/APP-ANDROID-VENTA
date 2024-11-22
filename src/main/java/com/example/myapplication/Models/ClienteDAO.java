package com.example.myapplication.Models;

public interface ClienteDAO {

    boolean insertarCliente(Cliente cliente);
    boolean existeUsuario(String usuario);
    boolean existeCorreo(String correo);
    boolean iniciarSesion(String usuario, String contrasena);


}