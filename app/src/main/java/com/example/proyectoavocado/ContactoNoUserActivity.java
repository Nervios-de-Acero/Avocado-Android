package com.example.proyectoavocado;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class ContactoNoUserActivity extends AppCompatActivity {

    private EditText etAsunto;
    private EditText etComentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_no_user);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageButton btnBack = findViewById(R.id.btn_volverInicio);
        Button btnEnviar = findViewById(R.id.btn_enviar);
        etAsunto = findViewById(R.id.etAsunto);
        etComentario = findViewById(R.id.etComentario);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir InicioActivity
                Intent intent = new Intent(ContactoNoUserActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String asunto = etAsunto.getText().toString().trim();
                String comentario = etComentario.getText().toString().trim();

                if (asunto.isEmpty() || comentario.isEmpty()) {
                    mostrarError("Por favor, complete ambos campos.");
                } else {
                    mostrarExito("Su consulta fue enviada.");
                }
            }
        });
    }

    private void mostrarError(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactoNoUserActivity.this);
        builder.setTitle("Error");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No necesitas hacer nada específico al hacer clic en Aceptar
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void mostrarExito(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ContactoNoUserActivity.this);
        builder.setTitle("Éxito");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Redirigir a InicioActivity después de que el usuario haga clic en Aceptar
                Intent intent = new Intent(ContactoNoUserActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
