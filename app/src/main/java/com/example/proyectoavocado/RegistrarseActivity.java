package com.example.proyectoavocado;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageButton;
import android.util.Patterns;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrarseActivity extends AppCompatActivity {

    private EditText etNombre;
    private EditText etEmail;
    private EditText etUsuario;
    private EditText etPassword;
    private EditText etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        //capturo los id de los botones
        ImageButton btnVolver = findViewById(R.id.btn_backInicio);
        Button btnRegistrarse = findViewById(R.id.btn_registrarse);

        //capturo datos de los edit text
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etUsuario= findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir InicioActivity
                Intent intent = new Intent(RegistrarseActivity.this, InicioActivity.class);
                startActivity(intent);
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir InicioActivity
                registrarse(etNombre.getText().toString(), etEmail.getText().toString(), etUsuario.getText().toString(), etPassword.getText().toString(), etConfirmPassword.getText().toString());
            }
        });
    }
    private void mostrarError(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
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

    private boolean validarNombre(String nombre) {
        if (nombre.matches("[a-zA-Z ]+")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validarUsuario(String usuario) {
        if (!usuario.contains(" ") && usuario.length() >= 5 && Pattern.matches("^[a-zA-Z0-9!@#$%^&*()_+-=]*$", usuario)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validarPassword(String password) {
        if (!password.contains(" ") && password.length() >= 8 && password.length() <= 24 &&
                Pattern.compile("[A-Z]").matcher(password).find() &&
                Pattern.compile("[a-z]").matcher(password).find() &&
                Pattern.compile("[!@#$%^&*()_+-=]").matcher(password).find()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validarEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            return false;
        }
    }

    private void registrarse(String nombre, String email, String usuario, String password, String confirmPassword){
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/registro";

        //Para validar llenado de campos
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(email) || TextUtils.isEmpty(usuario) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            mostrarError("Por favor, complete todos los campos.");
            return;
        }

        if (!validarNombre(nombre)) {
            mostrarError("El Nombre solo puede contener letras y espacios");
            return;
        }

        if (!validarEmail(email)) {
            mostrarError("El Correo Electrónico ingresado no es válido");
            return;
        }

        if (!validarUsuario(usuario)) {
            mostrarError("El Usuario no puede contener espacios y debe tener al menos 5 caracteres");
            return;
        }

        if (!validarPassword(password)) {
            mostrarError("La Contraseña debe tener entre 8 y 24 caracteres, una mayúscula, una minúscula, un caracter especial y un número");
            return;
        }

        if (!password.equals(confirmPassword)) {
            mostrarError("Las contraseñas no coinciden");
            return;
        }

        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    Boolean success = json.getBoolean("success");
                    String msg = "";

                    if (!success){
                        if (json.has("content")) {
                            JSONArray contentArray = json.getJSONArray("content");

                            for (int i = 0; i < contentArray.length(); i++) {
                                JSONObject contentObject = contentArray.getJSONObject(i); // Obtenemos el primer objeto del arreglo "content"
                                String mensaje = contentObject.getString("msg");
                                msg = mensaje + "\n\n" + msg;
                            }
                        } else {
                            msg = json.getString("message");
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage(msg);
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    } else {

                        msg = json.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrarseActivity.this);
                        builder.setMessage(msg);
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(RegistrarseActivity.this, InicioActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    }


                } catch (JSONException e) {
                    //Modificar el mensaje para personalizarlo (mensaje para logcat)
                    Log.e("Error en la request", "Error al traer los datos: " + e.getMessage());
                    throw new RuntimeException("Error al traer los datos");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                if (errorMessage != null) {
                    Log.e("Error", errorMessage);
                } else {
                    Log.e("Error", "Mensaje de error nulo");
                }
            }
        })

    {
        protected Map<String, String> getParams(){
            Map<String, String> params = new HashMap<>();
            params.put("nombreCompleto", nombre);
            params.put("email", email);
            params.put("usuario", usuario);
            params.put("password", password);
            return params;
        }
    };
        // El “get” que está dentro de la función add es el StringRequest
        // que está más arriba, por si le cambian el nombre
        Volley.newRequestQueue(this).add(post);
    }

}