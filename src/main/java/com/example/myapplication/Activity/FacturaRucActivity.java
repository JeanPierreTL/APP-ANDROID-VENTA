package com.example.myapplication.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.ApiService;
import com.example.myapplication.Models.ItemCarrito;
import com.example.myapplication.Models.RucResponse;
import com.example.myapplication.Models.DniResponse;
import com.example.myapplication.Models.CarritoSingleton;
import com.example.myapplication.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FacturaRucActivity extends AppCompatActivity {

    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImNvZGVhbmltZTRAZ21haWwuY29tIn0.fhwwTKBPENdcJL-tTzB36QcVYsdeDH9LclgxKQ0PLC4";

    private EditText editTextId;
    private TextView textFacturaNombre, textFacturaProductos, textFacturaIgv, textFacturaTotal;
    private LinearLayout layoutFacturaDetalles;
    private Button buttonDni, buttonRuc, buttonAceptar;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_ruc);

        // Inicializar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dniruc.apisperu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        // Inicializar vistas
        editTextId = findViewById(R.id.editTextId);
        textFacturaNombre = findViewById(R.id.textFacturaNombre);
        textFacturaProductos = findViewById(R.id.textFacturaProductos);
        textFacturaIgv = findViewById(R.id.textFacturaIgv);
        textFacturaTotal = findViewById(R.id.textFacturaTotal);
        layoutFacturaDetalles = findViewById(R.id.layoutFacturaDetalles);
        buttonDni = findViewById(R.id.buttonDni);
        buttonRuc = findViewById(R.id.buttonRuc);
        buttonAceptar = findViewById(R.id.buttonAceptar);

        // Botones DNI y RUC
        buttonDni.setOnClickListener(v -> {
            editTextId.setHint("Ingrese DNI");
            editTextId.setVisibility(View.VISIBLE);
            buttonAceptar.setVisibility(View.VISIBLE);
        });

        buttonRuc.setOnClickListener(v -> {
            editTextId.setHint("Ingrese RUC");
            editTextId.setVisibility(View.VISIBLE);
            buttonAceptar.setVisibility(View.VISIBLE);
        });

        // Botón Aceptar
        buttonAceptar.setOnClickListener(v -> {
            String id = editTextId.getText().toString();
            if (id.isEmpty()) {
                showError("Por favor, ingrese un DNI o RUC válido.");
                return;
            }

            if (id.length() == 8) {
                consultaDni(id);
            } else if (id.length() == 11) {
                consultaRuc(id);
            } else {
                showError("Por favor, ingrese un número válido.");
            }
        });
    }

    private void consultaDni(String dni) {
        Call<DniResponse> call = apiService.getDniInfo(dni, TOKEN);
        call.enqueue(new Callback<DniResponse>() {
            @Override
            public void onResponse(Call<DniResponse> call, Response<DniResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DniResponse dniResponse = response.body();
                    // Actualizar el layout de detalles con el nombre del cliente
                    actualizarFactura(dniResponse.getNombres() + " " +
                            dniResponse.getApellidoPaterno() + " " +
                            dniResponse.getApellidoMaterno());
                } else {
                    showError("Error al obtener los datos del DNI.");
                }
            }

            @Override
            public void onFailure(Call<DniResponse> call, Throwable t) {
                showError("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void consultaRuc(String ruc) {
        Call<RucResponse> call = apiService.getRucInfo(ruc, TOKEN);
        call.enqueue(new Callback<RucResponse>() {
            @Override
            public void onResponse(Call<RucResponse> call, Response<RucResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RucResponse rucResponse = response.body();
                    // Actualizar el layout de detalles con la razón social del cliente
                    actualizarFactura(rucResponse.getRazonSocial());
                } else {
                    showError("Error al obtener los datos del RUC.");
                }
            }

            @Override
            public void onFailure(Call<RucResponse> call, Throwable t) {
                showError("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void actualizarFactura(String nombre) {
        // Mostrar el nombre del cliente (ya sea del DNI o RUC)
        textFacturaNombre.setText("Razón Social / Cliente: " + nombre);

        // Obtener los productos del carrito
        double subtotal = 0.0;
        StringBuilder productos = new StringBuilder("Productos:\n");

        // Recorrer los productos del carrito
        for (ItemCarrito item : CarritoSingleton.getInstance().getItems()) {
            double totalProducto = item.getProducto().getPrecio() * item.getCantidad();
            productos.append(item.getProducto().getNombreProducto())
                    .append(" - ").append(item.getCantidad())
                    .append(" unidades - S/ ").append(totalProducto).append("\n");
            subtotal += totalProducto;
        }

        // Calcular el IGV (18%) y el total
        double igv = subtotal * 0.18;
        double total = subtotal + igv;

        // Actualizar los campos de la factura
        textFacturaProductos.setText(productos.toString());
        textFacturaIgv.setText("IGV: S/ " + String.format("%.2f", igv));
        textFacturaTotal.setText("Total: S/ " + String.format("%.2f", total));

        // Hacer visible el layout de la factura
        layoutFacturaDetalles.setVisibility(View.VISIBLE);
    }

    private void showError(String mensaje) {
        textFacturaNombre.setText(mensaje);
        layoutFacturaDetalles.setVisibility(View.GONE);
    }
}
