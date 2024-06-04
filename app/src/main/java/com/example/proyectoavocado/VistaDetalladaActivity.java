package com.example.proyectoavocado;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoavocado.controllers.Ingrediente;
import com.example.proyectoavocado.controllers.Receta;
import com.example.proyectoavocado.reciclesAdaptadores.IngredienteViewAdapter;
import com.example.proyectoavocado.reciclesAdaptadores.PasoViewAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class VistaDetalladaActivity extends AppCompatActivity {

    private Integer recetaIdEspecifica;
    private String emailUsuario;
    private TextView tituloReceta;
    private TextView creadoPor;
    private TextView descripcionView;
    private TextView tiempoCoccionView;
    private TextView dificultadView;
    private RecyclerView recyclerIngrediente;
    private RecyclerView recyclerPaso;
    private ImageView recipeImage;
    private List<String> pasosList;
    private ImageButton btnMenuReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_detallada);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //capturo los id de los botones
        ImageButton btnBack = findViewById(R.id.btn_backFeed);
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnBuscarReceta = findViewById(R.id.btn_buscar);
        ImageButton btnAgregarReceta = findViewById(R.id.btn_agregar);
        ImageButton btnSuscripcion = findViewById(R.id.btn_suscripcion);
        ImageButton btnPerfil = findViewById(R.id.btn_perfil);
        btnMenuReceta = findViewById(R.id.btn_menu_receta);

        // Instanciar las variables de la interfaz de usuario
        tituloReceta = findViewById(R.id.tituloReceta);
        creadoPor = findViewById(R.id.creadoPor);
        descripcionView = findViewById(R.id.descripcionView);
        tiempoCoccionView = findViewById(R.id.coccionView);
        dificultadView = findViewById(R.id.dificultadView);

        // Instanciar los RecyclerView
        recyclerIngrediente = findViewById(R.id.recyclerIngrediente);
        recyclerPaso = findViewById(R.id.recyclerPaso);

        // Configurar el LayoutManager para los RecyclerView
        LinearLayoutManager layoutManagerIngrediente = new LinearLayoutManager(this);
        LinearLayoutManager layoutManagerPaso = new LinearLayoutManager(this);
        recyclerIngrediente.setLayoutManager(layoutManagerIngrediente);
        recyclerPaso.setLayoutManager(layoutManagerPaso);

        recetaIdEspecifica = getIntent().getIntExtra("receta_id", -1);
        if (recetaIdEspecifica != -1) {
            obtenerDetallesReceta(recetaIdEspecifica);
        } else {
            // Manejar el caso cuando no se proporciona el ID de la receta
            handleError("ID de receta no proporcionado");
            // Finalizar la actividad actual si no hay un ID de receta para validar
            finish();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir FeedActivity
                Intent intent = new Intent(VistaDetalladaActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir FeedActivity
                Intent intent = new Intent(VistaDetalladaActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        btnAgregarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir AgregarRecetaActivity
                Intent intent = new Intent(VistaDetalladaActivity.this, AgregaRecetaActivity.class);
                startActivity(intent);
            }
        });

        btnSuscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir WebRedirectActivity
                Intent intent = new Intent(VistaDetalladaActivity.this, WebRedirectActivity.class);
                startActivity(intent);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir PerfilActivity
                Intent intent = new Intent(VistaDetalladaActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        btnMenuReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    @SuppressLint("ResourceType")
    private void showPopupMenu(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customMenuView = getLayoutInflater().inflate(R.layout.menu_receta, null);

        TextView textViewEditar = customMenuView.findViewById(R.id.menu_modificar);
        TextView textViewEliminar = customMenuView.findViewById(R.id.menu_eliminar);

        textViewEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para editar la receta
                // Abre la actividad de edición (ModificarRecetaActivity)
                Intent intentEditar = new Intent(VistaDetalladaActivity.this, ModificarRecetaActivity.class);
                // Pasa el ID de la receta a la actividad de edición si es necesario
                intentEditar.putExtra("receta_id", recetaIdEspecifica);
                startActivity(intentEditar);
            }
        });

        textViewEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para eliminar la receta
                // Elimina la receta de la base de datos u otras operaciones necesarias
                eliminarReceta(emailUsuario, recetaIdEspecifica);
            }
        });

        builder.setView(customMenuView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public class RecetasUsuarioRequest {
        private String email;

        public RecetasUsuarioRequest(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }

    public class RecetasUsuarioResponse {
        private List<Receta> recetas;

        public List<Receta> getRecetas() {
            return recetas;
        }
    }

    private void obtenerDetallesReceta(Integer recetaId) {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/receta/getRecetaById/" + recetaId;

        // Realizar la solicitud GET al servidor para obtener los detalles de la receta por su ID
        StringRequest get = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    // Parsear la respuesta JSON para obtener los detalles de la receta
                    String titulo = json.getString("titulo");
                    String nombreUsuario = json.getString("creadoPor");
                    String descripcion = json.getString("descripcion");
                    String tiempoCoccion = json.getString("tiempoCoccion");
                    String dificultad = json.getString("dificultad");
                    String imagen = json.getString("imagen");
                    String emailCreadoPor = json.getString("emailCreadoPor");

                    SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    String emailSp = sharedPreferences.getString("email", "");

                    if (emailSp.equals(emailCreadoPor)) {
                        btnMenuReceta.setVisibility(View.VISIBLE);
                    }

                    // Obtener el array de ingredientes y pasos
                    // Obtener el array de ingredientes y pasos
                    if (!json.isNull("ingredientes")) {
                        JSONArray ingredientesArray = json.getJSONArray("ingredientes");

                        Log.d("Entra ingredientes", String.valueOf(ingredientesArray));
                        List<Ingrediente> ingredientesList = new ArrayList<>();
                        for (int i = 0; i < ingredientesArray.length(); i++) {
                            String nombreIngrediente = ingredientesArray.getString(i);
                            Ingrediente ingrediente = new Ingrediente(nombreIngrediente);
                            ingredientesList.add(ingrediente);
                        }
                        // Configurar adaptadores y asignar a RecyclerViews
                        IngredienteViewAdapter ingredienteAdapter = new IngredienteViewAdapter(ingredientesList);
                        recyclerIngrediente.setAdapter(ingredienteAdapter);
                    } else {
                        TextView sinIngredientes = findViewById(R.id.sinIngredientes);
                        sinIngredientes.setVisibility(View.VISIBLE);
                        recyclerIngrediente.setVisibility(View.GONE);
                    }

                    // Obtener y procesar el array de pasos
                    if (!json.isNull("pasos")) {
                        JSONArray pasosArray = json.getJSONArray("pasos");
                        pasosList = new ArrayList<String>();
                        for (int i = 0; i < pasosArray.length(); i++) {
                            String descripcionPaso = pasosArray.getString(i);
                            // Agregar la descripción del paso a la lista de pasos
                            pasosList.add(descripcionPaso);
                        }
                        // Configurar el adaptador de pasos y asignarlo al RecyclerView
                        PasoViewAdapter pasoAdapter = new PasoViewAdapter(pasosList);
                        recyclerPaso.setAdapter(pasoAdapter);
                    } else {
                        handleError("El campo 'pasos' en la respuesta es nulo o no es un JSONArray válido.");
                        TextView sinPasos = findViewById(R.id.sinPasos);
                        sinPasos.setVisibility(View.VISIBLE);
                        recyclerPaso.setVisibility(View.GONE);
                    }

                    // Mostrar los detalles en los TextViews del layout
                    tituloReceta.setText(titulo);
                    creadoPor.setText(nombreUsuario);
                    descripcionView.setText(descripcion);
                    tiempoCoccionView.setText(tiempoCoccion);
                    dificultadView.setText(dificultad);

                    recipeImage = findViewById(R.id.recipeImage);
                    if (!json.isNull("imagen") && !imagen.equals("null")) {
                        Picasso.get().load(imagen).fit().centerInside().into(recipeImage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    handleError("Error al procesar la respuesta del servidor");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        handleError("Error en la solicitud: " + error.getMessage());
                    }
                });

        // Agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(get);
    }

    private void handleError(String errorMessage) {
        // Lógica para manejar el error, por ejemplo, mostrar un mensaje al usuario
        Log.e("Error", errorMessage);
        // También puedes mostrar un Toast o un AlertDialog con el mensaje de error
    }

    private void eliminarReceta(String emailUsuario, Integer recetaId) {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/receta/eliminarReceta/" + recetaId + "?_method=DELETE";

        // Puedes incluir el correo electrónico del usuario en el cuerpo de la solicitud
        // si es necesario para el servidor
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", emailUsuario);
        } catch (JSONException e) {
            e.printStackTrace();
            handleError("Error al construir el cuerpo de la solicitud: " + e.getMessage());
            return;
        }
        // Crear una solicitud POST con el cuerpo de la solicitud y el ID de la receta en la URL
        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta del servidor
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            if (success) {
                                // La receta se eliminó correctamente
                                showToast("Receta eliminada: " + message);
                                Intent intent = new Intent(VistaDetalladaActivity.this, FeedActivity.class);
                                startActivity(intent);
                            } else {
                                // No se pudo eliminar la receta
                                showToast("Error al eliminar la receta: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Manejar errores al parsear la respuesta JSON
                            showToast("Error al procesar la respuesta del servidor");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        handleError("Error en la solicitud: " + error.getMessage());
                    }
                });

        // Agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(deleteRequest);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}