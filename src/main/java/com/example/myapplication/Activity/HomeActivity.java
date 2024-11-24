package com.example.myapplication.Activity;
import  com.example.myapplication.Models.Producto;
import  android.os.Bundle;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.Models.ClienteDAOImpl;
import com.example.myapplication.Models.SQLServerConnector;
import com.example.myapplication.R;
import  android.widget.LinearLayout;
import com.example.myapplication.Models.ProductoDAO;
import com.example.myapplication.Models.ProductoDAOImpl;
public class HomeActivity extends AppCompatActivity {

    private LinearLayout productContainer;
    private ProductoDAO productoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        productContainer = findViewById(R.id.productContainer);

        SQLServerConnector sqlConecctor = new SQLServerConnector();
        productoDAO = new ProductoDAOImpl(sqlConecctor);
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();

        if (productos.isEmpty()) {
            Toast.makeText(this, "No hay productos disponibles", Toast.LENGTH_SHORT).show();
        } else {
            agregarProductosDinamicamente(productos); // Crear botones dinámicos
        }

    }

    private void agregarProductosDinamicamente(List<Producto> productos) {
        LayoutInflater inflater = LayoutInflater.from(this); // Crea el inflater

        for (Producto producto : productos) {
            // Inflar el diseño del molde
            View productView = inflater.inflate(R.layout.item_producto, productContainer, false);

            // Obtener las referencias a los elementos del diseño
            ImageView productImage = productView.findViewById(R.id.productImage);
            TextView productName = productView.findViewById(R.id.productName);
            TextView productPrice = productView.findViewById(R.id.productPrice);

            // Configurar los datos del producto
            productName.setText(producto.getNombreProducto());
            productPrice.setText("S/ " + producto.getPrecio());

            // Configurar la imagen del producto
            if (producto.getImagen() != null && !producto.getImagen().isEmpty()) {
                int imageResId = getResources().getIdentifier(producto.getImagen(), "drawable", getPackageName());
                if (imageResId != 0) {
                    productImage.setImageResource(imageResId); // Imagen desde drawable
                } else {
                    productImage.setImageResource(R.drawable.ic_launcher_foreground); // Imagen por defecto
                }
            }

            // Configurar la acción al hacer clic en todo el recuadro
            productView.setOnClickListener(v -> {
                Toast.makeText(this, "Producto seleccionado: " + producto.getNombreProducto(), Toast.LENGTH_SHORT).show();
                // Aquí puedes implementar la lógica para agregar al carrito, abrir detalles, etc.
            });

            // Agregar la vista del producto al contenedor principal
            productContainer.addView(productView);
        }
    }





}