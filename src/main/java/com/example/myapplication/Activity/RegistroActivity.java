package com.example.myapplication.Activity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.Cliente;
import com.example.myapplication.Models.ClienteDAO;
import com.example.myapplication.Models.ClienteDAOImpl;
import com.example.myapplication.R;
import com.example.myapplication.Models.SQLServerConnector;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {


    EditText nombreEditText, usuarioEditText, contrasenaEditText, correoEditText, telefonoEditText,uPedidoEditText;
    Button btnRegistrarCliente;
    ClienteDAO clienteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.registro);

        nombreEditText = findViewById(R.id.nombre);
        usuarioEditText = findViewById(R.id.usuario);
        contrasenaEditText = findViewById(R.id.contrasena);
        correoEditText = findViewById(R.id.correo);
        telefonoEditText = findViewById(R.id.telefono);
        uPedidoEditText = findViewById(R.id.uPedido);
        btnRegistrarCliente = findViewById(R.id.btnRegistrar);
        // Configurar el DAO
        SQLServerConnector sqlConecctor = new SQLServerConnector();
        clienteDAO = new ClienteDAOImpl(sqlConecctor);

        btnRegistrarCliente.setOnClickListener(v -> {
            String nombre = nombreEditText.getText().toString();
            String usuario = usuarioEditText.getText().toString();
            String contrasena = contrasenaEditText.getText().toString();
            String correo = correoEditText.getText().toString();
            String telefono = telefonoEditText.getText().toString();
            String uPedido = uPedidoEditText.getText().toString();

            if (validarCampos(nombre, usuario, contrasena, correo, telefono, uPedido)) {
                Cliente cliente = new Cliente(nombre, usuario, contrasena, correo, telefono, uPedido);
                boolean resultado = clienteDAO.insertarCliente(cliente);
                if (resultado) {
                    mostrarDialogoAviso("¡Exito!");
                    new android.os.Handler().postDelayed(() -> {
                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // Opcional: Finaliza la actividad actual
                    }, 2500);
                } else {
                    Toast.makeText(this, "Error al registrar cliente", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean validarCampos(String nombre, String usuario, String contrasena, String correo, String telefono, String uPedido) {
        if (nombre.isEmpty()) {
            nombreEditText.setError("El nombre es obligatorio");
            nombreEditText.requestFocus();
            return false;
        } else if (nombre.length() < 3) { // El nombre debe tener al menos 3 caracteres
            nombreEditText.setError("El nombre debe tener al menos 3 caracteres");
            nombreEditText.requestFocus();
            return false;
        }

        if (usuario.isEmpty()) {
            usuarioEditText.setError("El usuario es obligatorio");
            usuarioEditText.requestFocus();
            return false;
        } else if (usuario.length() < 5) { // El usuario debe tener al menos 5 caracteres
            usuarioEditText.setError("El usuario debe tener al menos 5 caracteres");
            usuarioEditText.requestFocus();
            return false;
        } else if (clienteDAO.existeUsuario(usuario)) { // Verificar si el usuario ya existe
            usuarioEditText.setError("El nombre de usuario ya está registrado");
            usuarioEditText.requestFocus();
            return false;
        }

        if (contrasena.isEmpty()) {
            contrasenaEditText.setError("La contraseña es obligatoria");
            contrasenaEditText.requestFocus();
            return false;
        } else if (contrasena.length() < 6) { // La contraseña debe tener al menos 6 caracteres
            contrasenaEditText.setError("La contraseña debe tener al menos 6 caracteres");
            contrasenaEditText.requestFocus();
            return false;
        } else if (!contrasena.matches(".*[A-Z].*")) { // Verificar que tenga al menos una letra mayúscula
            contrasenaEditText.setError("La contraseña debe incluir al menos una letra mayúscula");
            contrasenaEditText.requestFocus();
            return false;
        } else if (!contrasena.matches(".*[a-z].*")) { // Verificar que tenga al menos una letra minúscula
            contrasenaEditText.setError("La contraseña debe incluir al menos una letra minúscula");
            contrasenaEditText.requestFocus();
            return false;
        } else if (!contrasena.matches(".*\\d.*")) { // Verificar que tenga al menos un número
            contrasenaEditText.setError("La contraseña debe incluir al menos un número");
            contrasenaEditText.requestFocus();
            return false;
        }

        if (correo.isEmpty()) {
            correoEditText.setError("El correo es obligatorio");
            correoEditText.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            correoEditText.setError("Formato de correo inválido");
            correoEditText.requestFocus();
            return false;
        } else if (clienteDAO.existeCorreo(correo)) { // Verificar si el correo ya existe
            correoEditText.setError("El correo ya está registrado");
            correoEditText.requestFocus();
            return false;
        }

        if (telefono.isEmpty()) {
            telefonoEditText.setError("El teléfono es obligatorio");
            telefonoEditText.requestFocus();
            return false;
        } else if (!telefono.matches("\\d{9}")) { // Verificar que tenga exactamente 9 dígitos
            telefonoEditText.setError("El teléfono debe tener exactamente 9 dígitos");
            telefonoEditText.requestFocus();
            return false;
        }

        if (uPedido.isEmpty()) {
            uPedidoEditText.setError("Este campo es obligatorio");
            uPedidoEditText.requestFocus();
            return false;
        }

        return true;
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