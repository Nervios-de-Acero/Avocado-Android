package com.example.proyectoavocado;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoavocado.controllers.Receta;
import com.example.proyectoavocado.reciclesAdaptadores.RecipeCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private SearchView searchView;
    private RecipeCardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //capturo los id de los botones
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnAgregarReceta = findViewById(R.id.btn_agregar);
        ImageButton  btnSuscripcion = findViewById(R.id.btn_suscripcion);
        ImageButton btnPerfil = findViewById(R.id.btn_perfil);
        ImageButton btnBuscar = findViewById(R.id.btn_buscar);
        //ID SearchView
        searchView = findViewById(R.id.searchView);

        //Cambiar la visibilidad del SeachView
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (searchView.getVisibility() == View.VISIBLE) {
                    searchView.setVisibility(View.INVISIBLE); // Ocultar el SearchView
                } else {
                    searchView.setVisibility(View.VISIBLE);   // Mostrar el SearchView
                }
            }
        });
        searchView.setOnQueryTextListener(this);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir FeedActivity
                Intent intent = new Intent(FeedActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        btnAgregarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir AgregarRecetaActivity
                Intent intent = new Intent(FeedActivity.this, AgregaRecetaActivity.class);
                startActivity(intent);
            }
        });

        btnSuscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir WebRedirectActivity
                Intent intent = new Intent(FeedActivity.this, WebRedirectActivity.class);
                startActivity(intent);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir PerfilActivity
                Intent intent = new Intent(FeedActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        conexion();
    }

    private void conexion() {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/receta/getRecetasFeed";

        StringRequest get = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    List<Receta> listaRecetas = parsearRespuesta(response);
                    setupRecyclerView(listaRecetas);
                } catch (JSONException e) {
                    handleError("Error al parsear los datos: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError("Error en la solicitud: " + error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(get);
    }

    private List<Receta> parsearRespuesta(String response) throws JSONException {
        Log.d("RESPONSE", response); // Verifica la respuesta del servidor
        List<Receta> listaRecetas = new ArrayList<>();

        // Parsear el objeto JSON principal
        JSONObject jsonResponse = new JSONObject(response);
        // Obtener el array JSON con la clave "content"
        JSONArray jsonArray = jsonResponse.getJSONArray("content");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            // Obtener datos de la receta del objeto JSON y crear un objeto Receta
            Integer idReceta = jsonObject.getInt("idReceta");
            String titulo = jsonObject.getString("titulo");
            String creadoPor = jsonObject.getString("creadoPor");
            String descripcion = jsonObject.getString("descripcion");
            // Puedes obtener otros campos de la receta de manera similar
            // ...

            // Crear objeto Receta y agregarlo a la lista
            Receta receta = new Receta(idReceta, titulo, creadoPor, descripcion);
            listaRecetas.add(receta);
        }
        return listaRecetas;
    }

    private void setupRecyclerView(List<Receta> listaRecetas) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecipeCardAdapter(listaRecetas, FeedActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(FeedActivity.this));
    }

    private void handleError(String errorMessage) {
        Log.e("Error en la request", errorMessage);
        // Muestra un mensaje de error al usuario si es necesario
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filtrado(newText);
        return false;
    }
}