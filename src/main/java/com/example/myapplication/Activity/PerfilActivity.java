package com.example.myapplication.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.Cliente;
import com.example.myapplication.Models.ClienteDAO;
import com.example.myapplication.Models.ClienteDAOImpl;
import com.example.myapplication.Models.SQLServerConnector;
import com.example.myapplication.R;
import com.example.myapplication.R;

public class PerfilActivity extends AppCompatActivity {
    private EditText etNombre, etUsuario, etCorreo, etTelefono;
    private Button btnGuardar;
    private ClienteDAO clienteDAO;
    private Cliente clienteLogeado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_perfil);
        etNombre = findViewById(R.id.etNombre);
        etUsuario = findViewById(R.id.etUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        btnGuardar = findViewById(R.id.btnGuardar);

        SQLServerConnector sqlConnector = new SQLServerConnector();
        clienteDAO = new ClienteDAOImpl(sqlConnector);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("usuario_logeado")) {
            String usuarioLogeado = intent.getStringExtra("usuario_logeado");
            clienteLogeado = clienteDAO.obtenerClientePorUsuario(usuarioLogeado);

            if (clienteLogeado != null) {
                cargarDatosUsuario();
            } else {
                Toast.makeText(this, "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se pudo obtener el usuario logeado", Toast.LENGTH_SHORT).show();
            finish(); // Cerrar la actividad si no se pasa un usuario
        }

        btnGuardar.setOnClickListener(v -> guardarCambios());



    }

    private void cargarDatosUsuario() {
        // Cargar los datos del cliente en los campos de texto
        if (clienteLogeado != null) {
            etNombre.setText(clienteLogeado.getNombre());
            etUsuario.setText(clienteLogeado.getUsuario());
            etCorreo.setText(clienteLogeado.getCorreo());
            etTelefono.setText(clienteLogeado.getTelefono());
            etUsuario.setEnabled(false); // Desactivar el campo de usuario para que no sea editable
        } else {
            Toast.makeText(this, "Error: No se pudieron cargar los datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarCambios() {
        // Obtener los datos actualizados de los campos
        String nuevoNombre = etNombre.getText().toString().trim();
        String nuevoCorreo = etCorreo.getText().toString().trim();
        String nuevoTelefono = etTelefono.getText().toString().trim();

        // Validar los campos
        if (nuevoNombre.isEmpty()) {
            etNombre.setError("El nombre no puede estar vacío");
            return;
        }
        if (nuevoCorreo.isEmpty()) {
            etCorreo.setError("El correo no puede estar vacío");
            return;
        }
        if (!nuevoTelefono.matches("\\d{9}")) {
            etTelefono.setError("El teléfono debe tener 9 dígitos");
            return;
        }

        // Mostrar un diálogo de confirmación con los datos actuales
        String mensajeConfirmacion = "Datos actuales:\n\n" +
                "Nombre: " + clienteLogeado.getNombre() + "\n" +
                "Correo: " + clienteLogeado.getCorreo() + "\n" +
                "Teléfono: " + clienteLogeado.getTelefono() + "\n\n" +
                "¿Deseas guardar los nuevos datos?";

        new AlertDialog.Builder(this)
                .setTitle("Confirmar cambios")
                .setMessage(mensajeConfirmacion)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    // Actualizar los datos en el cliente logeado
                    clienteLogeado.setNombre(nuevoNombre);
                    clienteLogeado.setCorreo(nuevoCorreo);
                    clienteLogeado.setTelefono(nuevoTelefono);

                    // Guardar en la base de datos
                    boolean resultado = clienteDAO.actualizarCliente(clienteLogeado);

                    if (resultado) {
                        mostrarDialogoAviso("¡Éxito!");

                        // Retrasar la navegación al LoginActivity
                        new android.os.Handler().postDelayed(() -> {
                            Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish(); // Opcional: Finaliza la actividad actual
                        }, 3000); // Retraso en milisegundos (3000 ms = 3 segundos)
                    } else {
                        Toast.makeText(this, "Error al guardar los cambios", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoAviso(String mensaje) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alerta_confirmacion);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true); // Permitir cerrar el diálogo al tocar fuera

        // Configurar el mensaje
       TextView dialogMessage = dialog.findViewById(R.id.dialogMessage);
        dialogMessage.setText(mensaje);

        // Mostrar el diálogo
        dialog.show();

        // Cerrar automáticamente después de 2 segundos
        new Handler().postDelayed(dialog::dismiss, 2000);
    }




















}



