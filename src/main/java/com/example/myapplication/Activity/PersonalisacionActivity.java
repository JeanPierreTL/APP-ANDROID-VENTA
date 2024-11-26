package com.example.myapplication.Activity;

import  android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.CarritoSingleton;
import com.example.myapplication.Models.Producto;
import com.example.myapplication.R;

import java.text.DecimalFormat;
    
    public class    PersonalisacionActivity extends AppCompatActivity {

        private Producto productoSeleccionado;
        private int cantidad = 1; // Cantidad inicial



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product_details);

            // Obtener referencias a los elementos de la UI
            ImageView productImage = findViewById(R.id.productImage);
            TextView productName = findViewById(R.id.productName);
            TextView productDescription = findViewById(R.id.productDescription);
            TextView productPrice = findViewById(R.id.productPrice);
            TextView productQuantity = findViewById(R.id.productQuantity);
            ImageButton decreaseQuantity = findViewById(R.id.decreaseQuantity);
            ImageButton increaseQuantity = findViewById(R.id.increaseQuantity);
            Button addToCartButton = findViewById(R.id.addToCartButton);

            // Obtener el producto seleccionado desde el Intent
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("producto")) {
                productoSeleccionado = (Producto) intent.getSerializableExtra("producto");

                // Configurar los datos del producto en la UI
                productName.setText(productoSeleccionado.getNombreProducto());
                productDescription.setText(productoSeleccionado.getDescripcion());
                productPrice.setText("S/ " + productoSeleccionado.getPrecio());

                // Configurar la imagen del producto
                if (productoSeleccionado.getImagen() != null) {
                    int imageResId = getResources().getIdentifier(productoSeleccionado.getImagen(), "drawable", getPackageName());
                    productImage.setImageResource(imageResId);
                }

                // Llamar a actualizarPrecio para inicializar el texto del botón con el precio inicial
                actualizarPrecio(addToCartButton);
            }

            // Configurar el incremento y decremento de cantidad
            decreaseQuantity.setOnClickListener(v -> {
                if (cantidad > 1) {
                    cantidad--;
                    productQuantity.setText(String.valueOf(cantidad));
                    actualizarPrecio(addToCartButton);
                }
            });

            increaseQuantity.setOnClickListener(v -> {
                cantidad++;
                productQuantity.setText(String.valueOf(cantidad));
                actualizarPrecio(addToCartButton);
            });

            // Configurar el botón para agregar al carrito
            addToCartButton.setOnClickListener(v -> {
                CarritoSingleton.getInstance().agregarProducto(productoSeleccionado, cantidad);
                Toast.makeText(this, "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
                Intent intenta = new Intent(PersonalisacionActivity.this, HomeActivity.class);
                intenta.putExtra("usuario_logeado", getIntent().getStringExtra("usuario_logeado")); // Propaga el dato
                startActivity(intenta);
            });

        }

        private void actualizarPrecio(Button addToCartButton) {
            double precioTotal = cantidad * productoSeleccionado.getPrecio();

            // Formatear el precio total a dos decimales
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String precioFormateado = decimalFormat.format(precioTotal);

            // Actualizar el texto del botón con el precio formateado
            addToCartButton.setText("Agregar S/ " + precioFormateado);
        }

    }








