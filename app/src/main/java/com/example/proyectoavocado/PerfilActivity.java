package com.example.proyectoavocado;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoavocado.controllers.Receta;
import com.example.proyectoavocado.reciclesAdaptadores.PerfilRecipeCardAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PerfilActivity extends AppCompatActivity {

    TextView text_nombre;
    TextView text_nombreusuario;
    ImageView imageViewPerfil;

    ImageButton boton_menuDesplegable;
    ImageButton btn_volver;
    SharedPreferences sharedPreferences;

    List<Receta> listaRecetasPerfil;

    Context self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        self = this;
        //capturo los id de los botones
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnBuscarReceta = findViewById(R.id.btn_buscar);
        ImageButton btnAgregarReceta = findViewById(R.id.btn_agregar);
        ImageButton btnSuscripcion = findViewById(R.id.btn_suscripcion);
        ImageButton btnPerfil = findViewById(R.id.btn_perfil);
        Button btnEditar = findViewById(R.id.btn_editar);
        btn_volver = findViewById(R.id.btn_volver);

        sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String emailSp = sharedPreferences.getString("email", "");
        traerDatosPerfil(emailSp);
        traerRecetas(emailSp);



        text_nombre = findViewById(R.id.text_nombre);
        text_nombreusuario = findViewById(R.id.text_nombreusuario);
        imageViewPerfil = findViewById(R.id.imageViewPerfil);
        ImageView menuIcon = findViewById(R.id.menu_icon);

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir FeedActivity
                Intent intent = new Intent(PerfilActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        btnAgregarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir AgregarRecetaActivity
                Intent intent = new Intent(PerfilActivity.this, AgregaRecetaActivity.class);
                startActivity(intent);
            }
        });

        btnSuscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir WebRedirectActivity
                Intent intent = new Intent(PerfilActivity.this, WebRedirectActivity.class);
                startActivity(intent);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir PerfilActivity
                Intent intent = new Intent(PerfilActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir ModificarPerfilActivity
                Intent intent = new Intent(PerfilActivity.this, ModificarPerfilActivity.class);
                startActivity(intent);
            }
        });

        // Establece un OnClickListener para el icono de menú
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

    }


    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menuContacto) {
            Intent intent = new Intent(this, ContactoActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.menuAcercaDe){
            Intent intent = new Intent(this, AcercaActivity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.menuCerrarSesion){
            SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, InicioActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
    // Método para mostrar el menú emergente
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Maneja las acciones correspondientes a cada ítem del menú
                switch (item.getItemId()) {
                    case 1:
                        startActivity(new Intent(PerfilActivity.this, ContactoActivity.class));
                        return true;
                    case 2:
                        startActivity(new Intent(PerfilActivity.this, AcercaActivity.class));
                        return true;
                    case 3:
                        // Aquí maneja el cierre de sesión
                        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(PerfilActivity.this, InicioActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Agrega las opciones del menú programáticamente
        popupMenu.getMenu().add(0, 1, 0, "Contacto");
        popupMenu.getMenu().add(0, 2, 0, "Acerca de");
        popupMenu.getMenu().add(0, 3, 0, "Cerrar Sesión");

        // Muestra el menú emergente
        popupMenu.show();
    }

    private void traerDatosPerfil(String userEmail){
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/usuario/getUsuario/" + userEmail;


        StringRequest get = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONObject content = json.getJSONObject("content");
                    String nombreCompleto = content.getString("nombreCompleto");
                    String usuario =  content.getString("usuario");
                    String imagen = content.getString("imagen");

                    if (imagen != null) {
                        // Si no hay errores al decodificar, muestra la imagen
                        Picasso.get().load(imagen).into(imageViewPerfil);
                    } else {
                        // Si no se pudo crear el Bitmap, muestra una imagen de perfil por defecto
                        imageViewPerfil.setImageResource(R.drawable.icono_perfil);
                    }

                    text_nombre.setText(nombreCompleto);
                    text_nombreusuario.setText(usuario);



                } catch (JSONException e) {
                    //Modificar el mensaje para personalizarlo (mensaje para logcat)
                    Log.e("Error en la request", "Error al traer los datos: " + e.getMessage());
                    mostrarError(e.getMessage());
                    throw new RuntimeException("Error al traer los datos");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                mostrarError(errorMessage);
                if (errorMessage != null) {
                    Log.e("Error", errorMessage);
                } else {
                    Log.e("Error", "Mensaje de error nulo");
                }
            }
        });
        Volley.newRequestQueue(this).add(get);
    }

    private void traerRecetas(String userEmail){
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/receta/getRecetasUsuario/" + userEmail ;

        RecyclerView recyclerPerfil = findViewById(R.id.recyclerPerfil);
        TextView noTienesRecetas = findViewById(R.id.noTienesRecetas);

        StringRequest post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject json = new JSONObject(response);
                    Boolean success = json.getBoolean("success");
                    String message = json.getString("message");
                    JSONArray content = json.getJSONArray("content");
                    Log.d("Contenido JSON", content.toString());

                    if(!success) {
                    // Imprimir "No tienes recetas en algún mensajito
                        recyclerPerfil.setVisibility(View.GONE);
                        noTienesRecetas.setVisibility(View.VISIBLE);

                    }
                    else {
                        listaRecetasPerfil = new ArrayList<>();
                        Log.d("CONTENT", String.valueOf(content));
                        for(int i = 0; i < content.length(); i++){
                            JSONObject el = content.getJSONObject(i);
                            int idReceta = el.getInt("idReceta");
                            String titulo = el.getString("titulo");
                            String imagen = el.getString("imagen");
                            listaRecetasPerfil.add(new Receta(idReceta, titulo, imagen));
                            // Agrega más recetas del perfil si es necesario

                            recyclerPerfil.setVisibility(View.VISIBLE);
                            noTienesRecetas.setVisibility(View.GONE);


                            // Crear un adaptador con la lista de recetas del perfil del usuario
                            PerfilRecipeCardAdapter adapter = new PerfilRecipeCardAdapter(listaRecetasPerfil, self);

                            // Configurar el RecyclerView con el adaptador
                            RecyclerView recyclerView = findViewById(R.id.recyclerPerfil);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(self));
                        }
                    }



                } catch (JSONException e) {
                    //Modificar el mensaje para personalizarlo (mensaje para logcat)
                    Log.e("Error en la request", "Error al traer los datos: " + e.getMessage());
                    mostrarError(e.getMessage());
                    throw new RuntimeException("Error al traer los datos");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = error.getMessage();
                mostrarError(errorMessage);
                if (errorMessage != null) {
                    Log.e("Error", errorMessage);
                } else {
                    Log.e("Error", "Mensaje de error nulo");
                }
            }
        });
        Volley.newRequestQueue(this).add(post);
    }

    private void mostrarError(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
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
}