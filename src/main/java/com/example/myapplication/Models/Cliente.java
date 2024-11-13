package com.example.myapplication.Models;

public class Cliente {

   private  int idCliente;
    private String nombre;
    private String usuario;
    private String contrasena;
    private String correo;
    private String telefono;
    private String uPedido;


    public Cliente(String nombre, String usuario, String contrasena, String correo, String telefono, String uPedido) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.correo = correo;
        this.telefono = telefono;
        this.uPedido = uPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getuPedido() {
        return uPedido;
    }

    public void setuPedido(String uPedido) {
        this.uPedido = uPedido;
    }




    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
