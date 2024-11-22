package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import com.example.myapplication.Models.ClienteDAO;
import com.example.myapplication.Models.ClienteDAOImpl;
import com.example.myapplication.Models.SQLServerConnector;
import com.example.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    EditText usuario, contrasena;
    Button btn_login;
    Button btn_nocuenta;
    ClienteDAO clienteDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);

        usuario = findViewById(R.id.emailEditText);
        contrasena = findViewById(R.id.passwordEditText);
        btn_login = findViewById(R.id.btn_login);
        btn_nocuenta = findViewById(R.id.registerno);

        btn_nocuenta.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
        btn_login.setOnClickListener(v -> iniciarSesion());
    }
    private void iniciarSesion() {
        String usuarioInput = usuario.getText().toString().trim();
        String contrasenaInput = contrasena.getText().toString().trim();
        SQLServerConnector sqlConecctor = new SQLServerConnector();
        clienteDAO = new ClienteDAOImpl(sqlConecctor);

        // Validar que los campos no estén vacíos
        if (usuarioInput.isEmpty() || contrasenaInput.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa ambos campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar credenciales utilizando ClienteDAO
        boolean esValido = clienteDAO.iniciarSesion(usuarioInput, contrasenaInput);

        if (esValido) {
            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            // Redirigir a HomeActivity al inicio de sesión exitoso
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Finalizar LoginActivity
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}
