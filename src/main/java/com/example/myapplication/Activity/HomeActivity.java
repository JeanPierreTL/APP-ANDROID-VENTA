package com.example.myapplication.Activity;
import  com.example.myapplication.Models.Producto;
import  android.os.Bundle;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
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
        for (Producto producto : productos) {
            // Crear un botón dinámico
            Button productButton = new Button(this);

            // Configurar el texto del botón
            productButton.setText(producto.getNombreProducto() + " - S/" + producto.getPrecio());
            productButton.setTag(producto); // Guardar el producto en el botón como tag


            // Configurar la acción al hacer clic en el botón
            productButton.setOnClickListener(v -> {
                Producto seleccionado = (Producto) v.getTag(); // Obtener el producto asociado

                // Aquí puedes implementar la lógica para agregar al carrito
            });

            // Agregar el botón al contenedor dinámico
            productContainer.addView(productButton);
        }
    }




}