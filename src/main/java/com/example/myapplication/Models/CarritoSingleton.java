package com.example.myapplication.Models;
import java.util.List;
import java.util.ArrayList;
public class CarritoSingleton {
    private static CarritoSingleton instancia;
    private     List<ItemCarrito> items;

    private CarritoSingleton() {
        items = new ArrayList<>();
    }

    public static CarritoSingleton getInstance() {
        if (instancia == null) {
            instancia = new CarritoSingleton();
        }
        return instancia;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        // Verificar si el producto ya está en el carrito
        for (ItemCarrito item : items) {
            if (item.getProducto().getIdProducto() == producto.getIdProducto()) {
                // Si ya existe, sumar la cantidad
                item.setCantidad(item.getCantidad() + cantidad);
                return; // Salir del método
            }
        }

        // Si no existe, agregar un nuevo ítem al carrito
        items.add(new ItemCarrito(producto, cantidad));
    }

    public double calcularTotal() {
        double total = 0.0;
        for (ItemCarrito item : items) {
            total += item.getCantidad() * item.getProducto().getPrecio();
        }
        return total;
    }
}

