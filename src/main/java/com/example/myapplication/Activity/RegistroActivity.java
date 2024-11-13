package com.example.myapplication.Activity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.Cliente;
import com.example.myapplication.Models.ClienteDAO;
import com.example.myapplication.Models.ClienteDAOImpl;
import com.example.myapplication.R;
import com.example.myapplication.Models.SQLServerConnector;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

            Cliente cliente = new Cliente(nombre, usuario, contrasena, correo, telefono , uPedido);
            boolean resultado = clienteDAO.insertarCliente(cliente);
            if (resultado) {
                Toast.makeText(this, "Cliente registrado exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al registrar cliente", Toast.LENGTH_SHORT).show();
            }



        });
    }
}