package com.example.myapplication.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.myapplication.Models.ApiService;
import com.example.myapplication.Models.DetallePedido;
import com.example.myapplication.Models.Pedido;
import com.example.myapplication.Models.PedidoDAO;
import com.example.myapplication.Models.ItemCarrito;
import com.example.myapplication.Models.Pago;
import com.example.myapplication.Models.PedidoDAOImpl;
import com.example.myapplication.Models.RucResponse;
import com.example.myapplication.Models.DniResponse;
import com.example.myapplication.Models.CarritoSingleton;
import com.example.myapplication.Models.SQLServerConnector;
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
    private Button buttonDni, buttonRuc, buttonAceptar, finalizarPagoButton, buttonEfectivo, buttonTarjeta;

    private ApiService apiService;
    private String metodoPagoSeleccionado = "TARJETA"; // Método de pago por defecto
    private String nombreCliente = ""; // Variable para almacenar el nombre del cliente
    boolean exito = false;
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
        finalizarPagoButton = findViewById(R.id.buttonFinalizarPago);
        editTextId = findViewById(R.id.editTextId);
        textFacturaNombre = findViewById(R.id.textFacturaNombre);
        textFacturaProductos = findViewById(R.id.textFacturaProductos);
        textFacturaIgv = findViewById(R.id.textFacturaIgv);
        textFacturaTotal = findViewById(R.id.textFacturaTotal);
        layoutFacturaDetalles = findViewById(R.id.layoutFacturaDetalles);
        buttonDni = findViewById(R.id.buttonDni);
        buttonRuc = findViewById(R.id.buttonRuc);
        buttonAceptar = findViewById(R.id.buttonAceptar);
        buttonEfectivo = findViewById(R.id.buttonEfectivo);
        buttonTarjeta = findViewById(R.id.buttonTarjeta);

        SQLServerConnector sqlConnector = new SQLServerConnector();
        PedidoDAO pedidoDAO = new PedidoDAOImpl(sqlConnector);

        // Configuración de botones
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

        buttonEfectivo.setOnClickListener(v -> {
            metodoPagoSeleccionado = "EFECTIVO";
            Toast.makeText(this, "Método de pago seleccionado: Efectivo", Toast.LENGTH_SHORT).show();
        });

        buttonTarjeta.setOnClickListener(v -> {
            metodoPagoSeleccionado = "TARJETA";
            Toast.makeText(this, "Método de pago seleccionado: Tarjeta", Toast.LENGTH_SHORT).show();
        });

        finalizarPagoButton.setOnClickListener(v -> finalizarPago(pedidoDAO));
    }

    private void consultaDni(String dni) {
        Call<DniResponse> call = apiService.getDniInfo(dni, TOKEN);
        call.enqueue(new Callback<DniResponse>() {
            @Override
            public void onResponse(Call<DniResponse> call, Response<DniResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DniResponse dniResponse = response.body();
                    nombreCliente = dniResponse.getNombres() + " " +
                            dniResponse.getApellidoPaterno() + " " +
                            dniResponse.getApellidoMaterno();
                    actualizarFactura(nombreCliente);
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
                    nombreCliente = rucResponse.getRazonSocial();
                    actualizarFactura(nombreCliente);
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
        textFacturaNombre.setText("Razón Social / Cliente: " + nombre);

        double subtotal = calcularSubtotal();
        double igv = subtotal * 0.18;
        double total = subtotal + igv;

        StringBuilder productos = new StringBuilder("Productos:\n");
        for (ItemCarrito item : CarritoSingleton.getInstance().getItems()) {
            double totalProducto = item.getProducto().getPrecio() * item.getCantidad();
            productos.append(item.getProducto().getNombreProducto())
                    .append(" - ").append(item.getCantidad())
                    .append(" unidades - S/ ").append(totalProducto).append("\n");
        }

        textFacturaProductos.setText(productos.toString());
        textFacturaIgv.setText("IGV: S/ " + String.format("%.2f", igv));
        textFacturaTotal.setText("Total: S/ " + String.format("%.2f", total));
        layoutFacturaDetalles.setVisibility(View.VISIBLE);
    }

    private void finalizarPago(PedidoDAO pedidoDAO) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Procesando pedido...");
        progressDialog.setCancelable(false); // Evita que se cierre accidentalmente
        progressDialog.show();

        // Ejecutar en un hilo separado
        new Thread(() -> {
            double subtotal = calcularSubtotal();
            double total = subtotal + subtotal * 0.18;
            String dniRuc = editTextId.getText().toString();
            Pedido pedido = new Pedido();
            pedido.setIdCliente(obtenerIdCliente());
            pedido.setEstadoPedido("PAGADO");
            pedido.setSubtotal(subtotal);
            pedido.setTotal(total);
            pedido.setModalidad("PRESENCIAL");
            pedido.setNombreCliente(nombreCliente); // Usar el nombre obtenido
            pedido.setDniRuc(dniRuc);



            int idPedido = pedidoDAO.insertarPedido(pedido);
            if (idPedido > 0) {
                for (ItemCarrito item : CarritoSingleton.getInstance().getItems()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setIdPedido(idPedido);
                    detalle.setIdProducto(item.getProducto().getIdProducto());
                    detalle.setCantidad(item.getCantidad());
                    detalle.setPrecioUnitario(item.getProducto().getPrecio());
                    detalle.setSubtotal(item.getProducto().getPrecio() * item.getCantidad());
                    pedidoDAO.insertarDetallePedido(detalle);
                }

                Pago pago = new Pago();
                pago.setIdPedido(idPedido);
                pago.setMonto(total);
                pago.setMetodoPago(metodoPagoSeleccionado);
                pago.setEstadoPago("COMPLETADO");
                pedidoDAO.insertarPago(pago);

                exito = true;
            }

            // Volver al hilo principal para actualizar la interfaz de usuario
            runOnUiThread(() -> {
                progressDialog.dismiss();
                if (exito) {
                    Toast.makeText(FacturaRucActivity.this, "Pago realizado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FacturaRucActivity.this, BienvenidoActivity.class); // Cambiar de actividad
                    startActivity(intent);
                } else {
                    Toast.makeText(FacturaRucActivity.this, "Error al procesar el pedido", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }


    private double calcularSubtotal() {
        double subtotal = 0.0;
        for (ItemCarrito item : CarritoSingleton.getInstance().getItems()) {
            subtotal += item.getProducto().getPrecio() * item.getCantidad();
        }
        return subtotal;
    }

    private int obtenerIdCliente() {
        return 1; // Simulación de ID de cliente
    }

    private void showError(String mensaje) {
        textFacturaNombre.setText(mensaje);
        layoutFacturaDetalles.setVisibility(View.GONE);
    }
}
