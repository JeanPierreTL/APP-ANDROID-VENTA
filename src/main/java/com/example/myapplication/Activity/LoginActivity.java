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
    import android.app.ProgressDialog;
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

            if (usuarioInput.isEmpty() || contrasenaInput.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa ambos campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear el ProgressDialog
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Iniciando sesión...");
            progressDialog.setCancelable(false); // Evita que se cierre accidentalmente
            progressDialog.show();

            // Ejecutar la lógica en un hilo secundario
            new Thread(() -> {
                SQLServerConnector sqlConnector = new SQLServerConnector();
                ClienteDAO clienteDAO = new ClienteDAOImpl(sqlConnector);

                boolean esValido = clienteDAO.iniciarSesion(usuarioInput, contrasenaInput);

                // Volver al hilo principal para actualizar la UI
                runOnUiThread(() -> {
                    progressDialog.dismiss(); // Cerrar el ProgressDialog
                    if (esValido) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("usuario_logeado", usuarioInput);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        }

    }
