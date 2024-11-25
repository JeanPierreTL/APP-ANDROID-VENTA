package com.example.myapplication.Models;

public class CarritoSingleton {
    private static Carrito carrito;

    public static Carrito getInstance() {
        if (carrito == null) {
            carrito = new Carrito();
        }
        return carrito;
    }
}
