package com.example.proyectoavocado;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //capturo los id de los botones
        Button btnRegistrarse = findViewById(R.id.btn_registrarse);
        Button btnIniciarSesion = findViewById(R.id.btn_iniciarsesión);
        Button btnContactar = findViewById(R.id.btn_contactar);

        //les agrego un evento de escuchar a los botones y les seteo el Intent para navegar
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir registrarseActivity
                Intent intent = new Intent(InicioActivity.this, RegistrarseActivity.class);
                startActivity(intent);
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir IniciarSesionActivity
                Intent intent = new Intent(InicioActivity.this, IniciarSesionActivity.class);
                startActivity(intent);
            }
        });

        btnContactar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir ContactoActivity
                Intent intent = new Intent(InicioActivity.this, ContactoNoUserActivity.class);
                startActivity(intent);
            }
        });


    }
}