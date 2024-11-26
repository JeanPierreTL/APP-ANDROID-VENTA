package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.CarritoAdapter;
import com.example.myapplication.Models.CarritoSingleton;
import com.example.myapplication.Models.ItemCarrito;
import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class CarritoActivity extends AppCompatActivity implements CarritoAdapter.OnCarritoChangeListener {
    private ListView carritoListView;
    private TextView totalPrice;
    private Button checkoutButton;
    private Button vaciarCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.carrito);
        String usuarioLogeado = getIntent().getStringExtra("usuario_logeado");

        carritoListView = findViewById(R.id.cartListView);
        totalPrice = findViewById(R.id.subtotalText);
        checkoutButton = findViewById(R.id.proceedToPayButton);
        vaciarCarrito = findViewById(R.id.emptyCartButton);

        // Obtener los ítems del carrito
        List<ItemCarrito> itemsCarrito = CarritoSingleton.getInstance().getItems();

        // Configurar el adaptador personalizado
        CarritoAdapter adapter = new CarritoAdapter(this, itemsCarrito, this);
        carritoListView.setAdapter(adapter);

        // Mostrar el total inicial
        actualizarTotal();

        // Botón para vaciar el carrito
        vaciarCarrito.setOnClickListener(v -> {
            itemsCarrito.clear(); // Vaciar el carrito
            adapter.notifyDataSetChanged(); // Actualizar la vista
            actualizarTotal(); // Actualizar el total
            Toast.makeText(this, "Carrito vacío", Toast.LENGTH_SHORT).show();
        });

        // Botón para finalizar la compra
        checkoutButton.setOnClickListener(v -> {
            if (itemsCarrito.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(CarritoActivity.this, FacturaRucActivity.class);
            intent.putExtra("usuario_logeado", getIntent().getStringExtra("usuario_logeado")); // Propaga el dato
            startActivity(intent);
        });
    }

    // Método para actualizar el total
    private void actualizarTotal() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double total = CarritoSingleton.getInstance().calcularTotal();
        totalPrice.setText("Total: S/ " + decimalFormat.format(total));
    }

    @Override
    public void onCarritoChange() {
        actualizarTotal(); // Actualizar el total cuando cambie el carrito
    }
}
