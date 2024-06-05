package com.example.proyectoavocado;



import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ModificarPerfilActivity extends AppCompatActivity {


    private AlertDialog dialog; // Usar Dialog en lugar de AlertDialog
    private EditText perfilPassword1;
    private EditText perfilPassword2;
    private TextView perfilEmail;
    private EditText perfilNombreCompleto;
    private EditText perfilNombreUsuario;
    private String nombrePlaceholder;
   private String usuarioPlaceholder;

   private TextView nuevoPass;

   private LinearLayout layoutEditContainer;
   private LinearLayout nuevaPassContainer;
    private ImageButton btnEditNombre;
    private ImageButton btnAceptarEditNombre;
    private ImageButton btnCancelEditNombre;
    private ImageButton btnCambiarContraseña;
    private ImageButton btnCancelCambiarContraseña;
    private ImageButton btnAceptarCambiarContraseña;
    // ImageButton btnSubirImagen;

    private ImageView perfilImagen;

    Bitmap bitmap;

    private static final int PICK_IMAGE_REQUEST = 1;

    SharedPreferences sharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_perfil);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //capturo los id de los botones
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnBuscarReceta = findViewById(R.id.btn_buscar);
        ImageButton btnAgregarReceta = findViewById(R.id.btn_agregar);
        ImageButton btnFavoritos = findViewById(R.id.btn_suscripcion);
        ImageButton btnPerfil = findViewById(R.id.btn_perfil);
        ImageButton btnBackPerfil = findViewById(R.id.btn_backPerfil);
        Button btnEliminarCuenta = findViewById(R.id.btn_eliminarCuenta);

        btnEditNombre = findViewById(R.id.btnEditNombre);
        layoutEditContainer = findViewById(R.id.layoutEditContainer);
        nuevaPassContainer = findViewById(R.id.nuevaPassContainer);
        btnAceptarEditNombre = findViewById(R.id.btnAceptarEditNombre);
        btnCancelEditNombre = findViewById(R.id.btnCancelEditNombre);
        btnCambiarContraseña = findViewById(R.id.btnCambiarContraseña);
        btnCancelCambiarContraseña = findViewById(R.id.btnCancelCambiarContraseña);
        btnAceptarCambiarContraseña = findViewById(R.id.btnAceptarCambiarContraseña);


        //EditTexts
        perfilPassword1 = findViewById(R.id.perfilPassword1);
        perfilPassword2 = findViewById(R.id.perfilPassword2);
        perfilEmail = findViewById(R.id.perfilEmail);
        perfilNombreCompleto = findViewById(R.id.perfilNombreCompleto);
        perfilNombreUsuario = findViewById(R.id.perfilNombreUsuario);
        nuevoPass = findViewById(R.id.nuevoPass);

        perfilImagen = findViewById(R.id.perfilImagen);

        //Deshabilitar los EditText
        perfilNombreCompleto.setEnabled(false);
        perfilNombreUsuario.setEnabled(false);
        perfilPassword1.setEnabled(false);
        nuevoPass.setVisibility(View.INVISIBLE);
        perfilPassword2.setVisibility(View.INVISIBLE);

        sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String emailSp = sharedPreferences.getString("email", "");
        traerDatosPerfil(emailSp);

        btnCancelCambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfilPassword1.setHint("*********");
                perfilPassword1.setEnabled(false);

                perfilPassword2.setEnabled(false);
                perfilPassword2.setVisibility(View.GONE);

                nuevoPass.setVisibility(View.INVISIBLE);

                btnCambiarContraseña.setVisibility(View.VISIBLE);
                nuevaPassContainer.setVisibility(View.GONE);
            }
        });

        btnCambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCambiarContraseña.setVisibility(View.GONE);
                perfilPassword2.setVisibility(View.VISIBLE);
                nuevaPassContainer.setVisibility(View.VISIBLE);

                perfilPassword1.setEnabled(true);
                perfilPassword2.setEnabled(true);

                nuevoPass.setVisibility(View.VISIBLE);

                perfilPassword1.setText("");
                perfilPassword2.setText("");

            }
        });

        btnEditNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEditNombre.setVisibility(View.GONE);
                layoutEditContainer.setVisibility(View.VISIBLE);
                btnAceptarEditNombre.setVisibility(View.VISIBLE);
                btnCancelEditNombre.setVisibility(View.VISIBLE);
                usuarioPlaceholder = perfilNombreUsuario.getText().toString();
                nombrePlaceholder = perfilNombreCompleto.getText().toString();
                perfilNombreCompleto.setEnabled(true);
                perfilNombreUsuario.setEnabled(true);
            }
        });

        btnCancelEditNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfilNombreUsuario.setText(usuarioPlaceholder);
                perfilNombreCompleto.setText(nombrePlaceholder);

                perfilNombreCompleto.setEnabled(false);
                perfilNombreUsuario.setEnabled(false);

                btnAceptarEditNombre.setVisibility(View.GONE);
                btnCancelEditNombre.setVisibility(View.GONE);
                layoutEditContainer.setVisibility(View.GONE);
                btnEditNombre.setVisibility(View.VISIBLE);
            }
        });

        btnAceptarCambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llamar actualizar pass
                cambiarContraseña();


            }
        });
        btnAceptarEditNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //llamar actualizar
                actualizarNombres(String.valueOf(perfilEmail));
              // cambiar visibilidad
                btnAceptarEditNombre.setVisibility(View.GONE);
                btnCancelEditNombre.setVisibility(View.GONE);
                layoutEditContainer.setVisibility(View.GONE);
                btnEditNombre.setVisibility(View.VISIBLE);
              // deshabilitar
                perfilNombreUsuario.setEnabled(false);
                perfilNombreCompleto.setEnabled(false);
            }
        });


        // Otros botones
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir FeedActivity
                Intent intent = new Intent(ModificarPerfilActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        btnAgregarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir AgregarRecetaActivity
                Intent intent = new Intent(ModificarPerfilActivity.this, AgregaRecetaActivity.class);
                startActivity(intent);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir PerfilActivity
                Intent intent = new Intent(ModificarPerfilActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        btnEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogEliminarCuenta();
            }
        });

        btnBackPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir PerfilActivity
                Intent intent = new Intent(ModificarPerfilActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
    }

    private void mostrarDialogEliminarCuenta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModificarPerfilActivity.this);
        // Inflar el diseño personalizado
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_eliminar_cuenta, null);
        builder.setView(dialogView);

        // capturo los id de los elementos
        Button positiveButton = dialogView.findViewById(R.id.btn_aceptar);
        Button negativeButton = dialogView.findViewById(R.id.btn_cancelar);

        // Configurar los clics de los botones
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implementa aquí la lógica para eliminar la cuenta
                eliminarCuenta();
                // Cierra el diálogo
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "No se puede cancelar", Toast.LENGTH_SHORT ).show();
                }
            }
        });

        // Crear el diálogo y asignarlo a la variable dialog
        dialog = builder.create();

        // Mostrar el diálogo
        dialog.show();
    }

    private void eliminarCuenta() {
        // Obtener el correo electrónico del usuario desde SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", null);


        if (userEmail != null) {
            // El correo electrónico del usuario está disponible, puedes enviar la solicitud para eliminar la cuenta

            String pc_ip = getResources().getString(R.string.pc_ip);
            String url = "http://" + pc_ip + ":3000/usuario/eliminar?_method=DELETE";

            // Crea un objeto JSON con el correo electrónico del usuario
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("email", userEmail);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Crea una solicitud POST con el cuerpo JSON
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Cuenta eliminada con éxito
                            Toast.makeText(getApplicationContext(), "Cuenta eliminada", Toast.LENGTH_SHORT).show();

                            // Limpiar datos de sesión (cerrar sesión)
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("email"); // Elimina el correo electrónico u otros datos de sesión que estés almacenando
                            editor.apply();

                            // Redirigir a la actividad de inicio
                            Intent intent = new Intent(ModificarPerfilActivity.this, InicioActivity.class);
                            startActivity(intent);
                            finish(); // Cierra la actividad actual para evitar volver atrás
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Error al eliminar la cuenta
                            Toast.makeText(getApplicationContext(), "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                            // Log de errores
                            Log.e("Error", "Error al eliminar la cuenta: " + error.getMessage());
                        }
                    });

            // Agregar la solicitud a la cola de solicitudes
            Volley.newRequestQueue(this).add(postRequest);
        } else {
            // El correo electrónico del usuario no está disponible en SharedPreferences, muestra un mensaje de error o maneja la situación como desees
            Toast.makeText(getApplicationContext(), "Error mail no existe", Toast.LENGTH_SHORT).show();
        }
    }

    private void traerDatosPerfil(String userEmail){
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/usuario/getUsuario/" + userEmail;


        StringRequest get = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String jsonString = json.toString();
                    JSONObject content = json.getJSONObject("content");
                    String nombreCompleto = content.getString("nombreCompleto");
                    String usuario =  content.getString("usuario");
                    String email =  content.getString("email");
                    String imagen = content.getString("imagen");

                    if (imagen != null) {
                        // Si no hay errores al decodificar, muestra la imagen
                       Picasso.get().load(imagen).into(perfilImagen);
                    } else {
                        // Si no se pudo crear el Bitmap, muestra una imagen de perfil por defecto
                        perfilImagen.setImageResource(R.drawable.icono_perfil);
                    }

                    perfilNombreCompleto.setText(nombreCompleto);
                    perfilNombreUsuario.setText(usuario);
                    perfilEmail.setText(email);



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
        });
        Volley.newRequestQueue(this).add(get);
    }

    private void actualizarNombres(String userEmail){
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/usuario/actualizarPerfil?_method=PUT";

        JSONObject datos = new JSONObject();
        try {
            datos.put("nombreCompleto", perfilNombreCompleto.getText().toString());
            datos.put("usuario", perfilNombreUsuario.getText().toString());
            datos.put("email", perfilEmail.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest post = new StringRequest(Request.Method.POST, url,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String message = json.getString("message");
                    Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_LONG).show();
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
        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return datos.toString().getBytes();
            }
        };

        Volley.newRequestQueue(this).add(post);
    }

    private void cambiarContraseña() {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/usuario/modificarPassword?_method=PUT";

        JSONObject datos = new JSONObject();
        try {
            datos.put("password", perfilPassword1.getText().toString());
            datos.put("nuevoPassword", perfilPassword2.getText().toString());
            datos.put("email", perfilEmail.getText().toString());
            Log.d("DEBUGGEO", "Entra al try de cambiarContraseña");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Drawable originalBackground;
                Log.d("DEBUGGEO", "Entra al string request");
                try {
                    Log.d("DEBUGUEO", "entra al try de onResponse");
                    JSONObject json = new JSONObject(response);
                    boolean success = json.getBoolean("success");
                    String message = json.getString("message");
                    Log.d("DEBUGUEO", "entra al try");
                    if (success) {
                        Log.d("ENTRÓ AL SUCCESS", "DEBERÍA CAMBIAR LA PASS");
                        // Update UI and shared preferences on successful password change
                        originalBackground = perfilPassword1.getBackground();
                        perfilPassword1.setText("");
                        perfilPassword2.setText("");
                        perfilPassword1.setEnabled(false);
                        perfilPassword2.setEnabled(false);
                        perfilPassword2.setVisibility(View.GONE);
                        nuevaPassContainer.setVisibility(View.GONE);
                        nuevoPass.setVisibility(View.INVISIBLE);
                        btnCambiarContraseña.setVisibility(View.VISIBLE);

                        // Update shared preferences with new password
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("contraseña", perfilPassword2.getText().toString());
                        editor.apply();

                        Toast toast = Toast.makeText(ModificarPerfilActivity.this, message, Toast.LENGTH_SHORT);
                        toast.show();

                    } else {
                        originalBackground = perfilPassword1.getBackground();
                        perfilPassword1.setBackgroundResource(R.drawable.borde_rojo);
                        perfilPassword2.setBackgroundResource(R.drawable.borde_rojo);


                        String errorContent = json.getJSONObject("content").toString();
                        Toast toast = Toast.makeText(ModificarPerfilActivity.this, message, Toast.LENGTH_SHORT);
                        toast.show();  // Call method to parse specific error message

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                perfilPassword1.setBackground(originalBackground);
                                perfilPassword2.setBackground(originalBackground);
                            }
                        }, 2000);
                    }
                } catch (JSONException e) {
                    Log.e("Error en la request", "Error al actualizar los datos: " + e.getMessage());
                    String parseado = parseErrorMessage(e.getMessage().toString());
                    mostrarError(parseado);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "";
                if (error instanceof NetworkError) {
                    errorMessage = "Error de red. Verifique su conexión a internet.";
                } else if (error instanceof ServerError) {
                    int statusCode = error.networkResponse.statusCode;
                    if (statusCode == 400) {  // Check for 400 Bad Request status code
                        try {
                            String errorContent = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject errorJson = new JSONObject(errorContent);
                            // Extract error details from the JSON response (e.g., validation errors)
                            String validationErrors = parseErrorMessage(errorJson.toString());
                            if (!validationErrors.isEmpty()) {
                                errorMessage = validationErrors;
                            } else {
                                errorMessage = "Error desconocido al actualizar contraseña.";
                            }
                        } catch (JSONException e) {
                            Log.e("Error al parsear error", "Error al parsear JSON de error: " + e.getMessage());
                            errorMessage = "Error desconocido al actualizar contraseña.";
                        }
                    } else {
                        errorMessage = "Error del servidor (" + statusCode + ").";
                    }
                } else if (error instanceof TimeoutError) {
                    errorMessage = "Error de tiempo de espera. Intente nuevamente más tarde.";
                } else if (error instanceof AuthFailureError) {
                    errorMessage = "Error de autenticación.";
                } else {
                    errorMessage = "Error desconocido.";
                }

                if (!errorMessage.isEmpty()) {
                    mostrarError(errorMessage);
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return datos.toString().getBytes();
            }
        };

        Volley.newRequestQueue(this).add(post);
    }



    private void mostrarError(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ModificarPerfilActivity.this);
        builder.setTitle("Error");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private String parseErrorMessage(String errorContent) {
        try {
            JSONObject errorJson = new JSONObject(errorContent);

            // Check if the JSON structure contains an "errors" array
            if (errorJson.has("content") && errorJson.getBoolean("success") == false) {
                StringBuilder errorMessageBuilder = new StringBuilder();
                JSONArray errorsArray = errorJson.getJSONArray("content");

                // Loop through each error object in the array
                for (int i = 0; i < errorsArray.length(); i++) {
                    JSONObject errorObject = errorsArray.getJSONObject(i);

                    // Extract and format the error message based on the structure
                    String message = errorObject.optString("msg", "");
                    String field = errorObject.optString("path", "");  // Optional field

                    if (!message.isEmpty()) {
                        if (!field.isEmpty()) {
                            errorMessageBuilder.append(field + ": ");
                        }
                        errorMessageBuilder.append(message).append("\n");
                    }
                }

                // Return the concatenated string with newline separators
                return errorMessageBuilder.toString().trim();
            } else {
                // If "errors" array is not found, try parsing the original content
                return "Error de estructura de array";  // Call a helper method for legacy format
            }
        } catch (JSONException e) {
            // Handle JSON parsing errors
            Log.e("Error al parsear error", "Error al parsear JSON de error: " + e.getMessage());
            return errorContent;  // Return original errorContent as fallback
        }
    }

}