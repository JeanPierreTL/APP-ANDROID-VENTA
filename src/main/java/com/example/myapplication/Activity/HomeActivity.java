package com.example.myapplication.Activity;
import com.example.myapplication.Models.Carrito;
import  com.example.myapplication.Models.Producto;

import android.content.Intent;
import  android.os.Bundle;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.widget.ImageButton;
import android.widget.SearchView;
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
    private List<Producto> productos; // Lista completa de productos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        productContainer = findViewById(R.id.productContainer);
        SearchView searchView = findViewById(R.id.searchView);
        ImageButton searchButton = findViewById(R.id.searchButton);
       ImageButton  perfilButton = findViewById(R.id.perfilButton);
       ImageButton CarritoButton = findViewById(R.id.CarritoButton);
        // Configuración de la base de datos y productos
        SQLServerConnector sqlConnector = new SQLServerConnector();
        productoDAO = new ProductoDAOImpl(sqlConnector);
        productos = productoDAO.obtenerTodosLosProductos();

        // Mostrar todos los productos inicialmente
        if (productos.isEmpty()) {
            Toast.makeText(this, "No hay productos disponibles", Toast.LENGTH_SHORT).show();
        } else {
            agregarProductosDinamicamente(productos); // Crear botones dinámicos
        }

        CarritoButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CarritoActivity.class);
            intent.putExtra("usuario_logeado", getIntent().getStringExtra("usuario_logeado")); // Propaga el dato
            startActivity(intent);
        });


        perfilButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
            intent.putExtra("usuario_logeado", getIntent().getStringExtra("usuario_logeado")); // Propaga el dato
            startActivity(intent);
        });

        // Configurar el botón de búsqueda para mostrar el SearchView
        searchButton.setOnClickListener(v -> {
            searchView.setVisibility(View.VISIBLE); // Mostrar el SearchView
            searchButton.setVisibility(View.GONE); // Ocultar el botón de búsqueda
            searchView.requestFocus(); // Dar foco al SearchView automáticamente
          productContainer.removeAllViews(); // Limpiar el contenedor
        });

        // Configurar el SearchView para filtrar productos
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Cuando el usuario confirma la búsqueda
                filtrarProductos(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Mientras el usuario escribe, se filtran los productos
                filtrarProductos(newText);
                return true;
            }
        });

        // Configurar el evento de cierre del SearchView
        searchView.setOnCloseListener(() -> {
            searchView.setVisibility(View.GONE); // Ocultar el SearchView
            searchButton.setVisibility(View.VISIBLE); // Mostrar el botón de búsqueda
            searchView.clearFocus(); // Remover el foco del SearchView
            actualizarProductos(productos); // Restaurar la lista completa limpiando primero el contenedor
            return true;
        });

    }

    // Método para filtrar productos
    private void filtrarProductos(String query) {
        List<Producto> productosFiltrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getNombreProducto().toLowerCase().contains(query.toLowerCase())) {
                productosFiltrados.add(producto);
            }
        }

        // Actualizar el contenedor con los productos filtrados
        actualizarProductos(productosFiltrados);
    }

    // Método para actualizar los productos en el contenedor dinámico
    private void actualizarProductos(List<Producto> productosFiltrados) {
        productContainer.removeAllViews(); // Limpiar el contenedor
        agregarProductosDinamicamente(productosFiltrados); // Agregar productos filtrados
    }

    // Método para agregar los productos dinámicamente
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
                Intent intent = new Intent(this, PersonalisacionActivity.class);
                intent.putExtra("producto", producto); // Enviar el producto al detalle
                startActivity(intent);
            });

            // Agregar la vista del producto al contenedor principal
            productContainer.addView(productView);
        }
    }



}

