package com.example.proyectoavocado;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoavocado.controllers.Ingrediente;
import com.example.proyectoavocado.controllers.Paso;
import com.example.proyectoavocado.controllers.Receta;
import com.example.proyectoavocado.reciclesAdaptadores.IngredienteRecipeAdapter;
import com.example.proyectoavocado.reciclesAdaptadores.PasosRecetaRecipeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModificarRecetaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewIngredientes, recyclerViewPasos;
    private IngredienteRecipeAdapter ingredienteAdapter;
    private PasosRecetaRecipeAdapter pasosAdapter;
    private List<Ingrediente> ingredientesList;
    private List<String> pasosList;
    private Integer recetaIdEspecifica;

    private EditText tituloReceta;
    private EditText editDescripcion;
    private EditText editTiempoCoccion;
    private EditText dificultadReceta;
    private ImageButton btnEditarTitulo;
    private ImageButton btnEditarDescripcionTiempoDificultad;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_receta);

        // Inicializar vistas y obtener correo electrónico del usuario
        // Inicializar vistas
        tituloReceta = findViewById(R.id.titulo_receta);
        editDescripcion = findViewById(R.id.editDescripcion);
        editTiempoCoccion = findViewById(R.id.editTiempo_coccion);
        dificultadReceta = findViewById(R.id.dificultad_receta);
        btnEditarTitulo = findViewById(R.id.btn_edit_tituloReceta);
        btnEditarDescripcionTiempoDificultad = findViewById(R.id.btn_editar_descripcion_coccion_dificultad);
        ImageButton btn_backFeed = findViewById(R.id.btn_backFeed);
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnAgregarReceta = findViewById(R.id.btn_agregar);
        ImageButton btnSuscripcion = findViewById(R.id.btn_suscripcion);
        ImageButton btnPerfil = findViewById(R.id.btn_perfil);

        // Inicializa las listas
        ingredientesList = new ArrayList<>();
        pasosList = new ArrayList<>();

        // Obtiene el ID de la receta que se va a modificar desde el intent
        recetaIdEspecifica = getIntent().getIntExtra("receta_id", -1);

        if (recetaIdEspecifica != -1) {
            // Carga los detalles de la receta desde la base de datos o desde donde estén almacenados
            //cargarDetallesReceta(recetaIdEspecifica);
        } else {
            // Maneja el caso donde no se proporciona el ID de la receta
            handleError("ID de receta no proporcionado");
            // Finaliza la actividad actual si no hay un ID de receta para validar
            finish();
        }
        Log.d("ID_RECETA", "ID de receta recibido: " + recetaIdEspecifica);
        obtenerDetallesReceta();

        // Inicializar RecyclerView
        recyclerViewIngredientes = findViewById(R.id.recycler_ingredientes);
        recyclerViewPasos = findViewById(R.id.recycler_pasos);

        // Configura el adaptador de ingredientes
        ingredienteAdapter = new IngredienteRecipeAdapter(ingredientesList, new IngredienteRecipeAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                ingredientesList.remove(position);
                ingredienteAdapter.notifyDataSetChanged();
            }
        });

        recyclerViewIngredientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIngredientes.setAdapter(ingredienteAdapter);

        // Configura el adaptador de pasos
        pasosAdapter = new PasosRecetaRecipeAdapter(pasosList, new PasosRecetaRecipeAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                pasosList.remove(position);
                pasosAdapter.notifyDataSetChanged();
            }
        });

        recyclerViewPasos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPasos.setAdapter(pasosAdapter);

        btnEditarTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Habilitar la edición del títuloReceta y mostrar un botón de guardar
                habilitarEdicion(tituloReceta, "titulo");
            }
        });

        btnEditarDescripcionTiempoDificultad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Habilitar la edición de editDescripcion, editTiempoCoccion y dificultadReceta
                // Mostrar un botón de guardar para guardar los cambios en la API cuando se haga clic en él
                habilitarEdicion(editDescripcion, "descripcion");
                habilitarEdicion(editTiempoCoccion, "tiempoCoccion");
                habilitarEdicion(dificultadReceta, "dificultad");
            }
        });

        // Capturar id del botón para agregar ingredientes y llamar al cuadro de diálogo
        Button btnAgregarIngrediente = findViewById(R.id.btn_agregarIngredientes);
        btnAgregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoIngredientes();
            }
        });


        // Capturar id del botón para agregar paso y llamar al cuadro de diálogo
        Button btnAgregarPaso = findViewById(R.id.btn_agregarPasoDialog);
        btnAgregarPaso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoPasos();
            }
        });

        // Botón para guardar los cambios en la receta
        ImageButton btnGuardarCambios = findViewById(R.id.btn_aceptar);
        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoTitulo = tituloReceta.getText().toString();
                String nuevaDescripcion = editDescripcion.getText().toString();
                String nuevoTiempo = editTiempoCoccion.getText().toString();
                String nuevaDificultad = dificultadReceta.getText().toString();

                List<Ingrediente> nuevosIngredientes = ingredienteAdapter.getIngredientes();
                List<String> nuevosPasos = pasosAdapter.getPasos();

                modificarTituloReceta(recetaIdEspecifica, nuevoTitulo);
                modificarDescripcionDificultadTiempo(recetaIdEspecifica, nuevaDescripcion, nuevoTiempo, nuevaDificultad);
                modificarIngredientes(recetaIdEspecifica, nuevosIngredientes);
                modificarPasos(recetaIdEspecifica, nuevosPasos);

                // Realizar acciones adicionales después de guardar los cambios

                // Finalizar la actividad después de guardar los cambios si es necesario
                finish();
            }
        });

        btn_backFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir PerfilActivity
                Intent intent = new Intent(ModificarRecetaActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir FeedActivity
                Intent intent = new Intent(ModificarRecetaActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        btnAgregarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir AgregarRecetaActivity
                Intent intent = new Intent(ModificarRecetaActivity.this, AgregaRecetaActivity.class);
                startActivity(intent);
            }
        });

        btnSuscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir WebRedirectActivity
                Intent intent = new Intent(ModificarRecetaActivity.this, WebRedirectActivity.class);
                startActivity(intent);
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un Intent para abrir PerfilActivity
                Intent intent = new Intent(ModificarRecetaActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });
    }

    private void habilitarEdicion(EditText editText, String campo) {
        // Habilitar la edición del EditText y mostrar un botón de guardar
        editText.setEnabled(true);
        editText.setFocusable(true);
        editText.requestFocus();
        // Mostrar un botón de guardar para guardar los cambios en la API cuando se haga clic en él
        // Puedes implementar esta lógica según tus necesidades
    }

    private void mostrarDialogoIngredientes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_agregar_ingrediente, null);
        builder.setView(dialogView);

        EditText editTextIngrediente = dialogView.findViewById(R.id.text_ingrediente);
        Button btnAgregarIngrediente = dialogView.findViewById(R.id.btn_agregarIngredienteLista);
        ImageButton btnCerrarDialog = dialogView.findViewById(R.id.btn_close_dialog);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnAgregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingrediente = editTextIngrediente.getText().toString().trim();
                if (!ingrediente.isEmpty()) {
                    // Agrega el ingrediente a la lista y actualiza el adaptador
                    Ingrediente nuevoIngrediente = new Ingrediente(ingrediente);
                    ingredientesList.add(nuevoIngrediente);
                    ingredienteAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    // Muestra un mensaje de error si el campo está vacío
                    Toast.makeText(ModificarRecetaActivity.this, "Por favor, ingresa un ingrediente", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCerrarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

   /* private void mostrarDialogoPasos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_agregar_paso, null);
        builder.setView(dialogView);

        //EditText editTextTituloPaso = dialogView.findViewById(R.id.text_tituloPaso);
        EditText editTextDescripcionPaso = dialogView.findViewById(R.id.text_descripcionPaso);
        Button btnAgregarPaso = dialogView.findViewById(R.id.btn_agregarPasoDialog);
        ImageButton btnCerrarDialogPaso = dialogView.findViewById(R.id.btn_close_dialogPaso);

        AlertDialog dialog = builder.create();
        dialog.show();


        btnAgregarPaso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String tituloPaso = editTextTituloPaso.getText().toString().trim();
                String descripcionPaso = editTextDescripcionPaso.getText().toString().trim();
                if (!tituloPaso.isEmpty() && !descripcionPaso.isEmpty()) {
                    // Agrega el paso a la lista y actualiza el adaptador
                    Paso nuevoPaso = new Paso(tituloPaso, descripcionPaso);
                    pasosList.add(nuevoPaso);
                    pasosAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    // Muestra un mensaje de error si alguno de los campos está vacío
                    Toast.makeText(ModificarRecetaActivity.this, "Por favor, completa todos los campos del paso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCerrarDialogPaso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }*/
   private void mostrarDialogoPasos() {
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       LayoutInflater inflater = getLayoutInflater();
       View dialogView = inflater.inflate(R.layout.dialog_agregar_paso, null);
       builder.setView(dialogView);

       EditText editTextDescripcionPaso = dialogView.findViewById(R.id.text_descripcionPaso);
       Button btnAgregarPaso = dialogView.findViewById(R.id.btn_agregarPasoDialog);
       ImageButton btnCerrarDialogPaso = dialogView.findViewById(R.id.btn_close_dialogPaso);

       AlertDialog dialog = builder.create();
       dialog.show();

       /*btnAgregarPaso.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String descripcionPaso = editTextDescripcionPaso.getText().toString().trim();
               if (!descripcionPaso.isEmpty()) {
                   // Agrega la descripción del paso a la lista y actualiza el adaptador
                   Paso nuevoPaso = new Paso(descripcion); // Solo necesitas la descripción
                   pasosList.add(nuevoPaso);
                   pasosAdapter.notifyDataSetChanged();
                   dialog.dismiss();
               } else {
                   // Muestra un mensaje de error si el campo está vacío
                   Toast.makeText(ModificarRecetaActivity.this, "Por favor, ingresa la descripción del paso", Toast.LENGTH_SHORT).show();
               }
           }
       });*/
       btnAgregarPaso.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String descripcionPaso = editTextDescripcionPaso.getText().toString().trim();
               if (!descripcionPaso.isEmpty()) {
                   // Agrega la descripción del paso a la lista
                   pasosList.add(descripcionPaso);

                   // Notifica al adaptador que los datos han cambiado
                   pasosAdapter.notifyDataSetChanged();

                   // Cierra el diálogo
                   dialog.dismiss();
               } else {
                   // Muestra un mensaje de error si el campo está vacío
                   Toast.makeText(ModificarRecetaActivity.this, "Por favor, ingresa la descripción del paso", Toast.LENGTH_SHORT).show();
               }
           }
       });

       btnCerrarDialogPaso.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog.dismiss();
           }
       });
   }

    private String getEmailUsuarioLogueado() {
        SharedPreferences sharedPreferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    private void obtenerDetallesReceta() {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String email = getEmailUsuarioLogueado(); // Obtén el email del usuario logueado desde SharedPreferences

        String url = "http://" + pc_ip + ":3000/receta/getRecetaById/" + recetaIdEspecifica;

        StringRequest get = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Parsea la respuesta para obtener los detalles de la receta del usuario
                    Receta receta = parsearRespuestaReceta(response);

                    // Carga los datos de la receta en los EditText correspondientes
                    cargarDatosReceta(receta);
                } catch (JSONException e) {
                    handleError("Error al parsear los datos de la receta: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleError("Error en la solicitud: " + error.getMessage());
            }
        });

        // Asegúrate de añadir la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(get);
    }

    private void cargarDatosReceta(Receta receta) {
        // Carga los datos de la receta en los EditText correspondientes
        tituloReceta.setText(receta.getTitulo());
        editDescripcion.setText(receta.getDescripcion());
        editTiempoCoccion.setText(receta.getTiempoCoccion());
        dificultadReceta.setText(receta.getDificultad());

        // Carga los ingredientes en el RecyclerView de ingredientes
        ingredientesList.addAll(receta.getIngredientes());
        ingredienteAdapter.notifyDataSetChanged();

        // Carga las descripciones de los pasos en el RecyclerView de pasos
        List<String> pasosStrings = receta.getPasos();
        pasosList.addAll(pasosStrings);
        pasosAdapter.notifyDataSetChanged();
    }

    private List<Ingrediente> obtenerListaDeIngredientes(JSONArray jsonArray) throws JSONException {
        List<Ingrediente> ingredientes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String nombreIngrediente = jsonArray.getString(i);
            Ingrediente ingrediente = new Ingrediente(nombreIngrediente);
            ingredientes.add(ingrediente);
        }
        return ingredientes;
    }

    /* private List<Paso> obtenerListaDePasos(JSONArray jsonArray) throws JSONException {
        List<Paso> pasos = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject pasoJson = jsonArray.getJSONObject(i);
            String titulo = pasoJson.getString("titulo");
            String descripcion = pasoJson.getString("descripcion");
            Paso paso = new Paso(titulo, descripcion);
            pasos.add(paso);
        }
        return pasos;
    }*/
    private JSONArray obtenerListaDePasos(List<String> pasos) throws JSONException {
        JSONArray pasosArray = new JSONArray();
        for (String paso : pasos) {
            pasosArray.put(paso);
        }
        return pasosArray;
    }

    private Receta parsearRespuestaReceta(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        // Obtén los valores del JSON
        Integer idReceta = jsonObject.getInt("idReceta");
        String titulo = jsonObject.getString("titulo");
        String descripcion = jsonObject.getString("descripcion");
        String tiempoCoccion = jsonObject.getString("tiempoCoccion");
        String dificultad = jsonObject.getString("dificultad");

        // Obtén la lista de ingredientes como lista de objetos Ingrediente
        List<Ingrediente> ingredientes = obtenerListaDeIngredientes(jsonObject.getJSONArray("ingredientes"));

        // Obtén la lista de pasos como lista de cadenas
        List<String> pasos = obtenerListaDeCadenas(jsonObject.getJSONArray("pasos"));

        // Crea y devuelve el objeto Receta
        return new Receta(idReceta, titulo, descripcion, tiempoCoccion, dificultad, ingredientes, pasos);
    }

    private List<String> obtenerListaDeCadenas(JSONArray jsonArray) throws JSONException {
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            lista.add(jsonArray.getString(i));
        }
        return lista;
    }

    private void handleError(String errorMessage) {
        // Aquí puedes manejar el error, por ejemplo, mostrando un mensaje al usuario o realizando alguna otra acción
        Log.e("Error", errorMessage);
        // También puedes mostrar un mensaje de error en un Toast
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    // Método para modificar el título de la receta
    private void modificarTituloReceta(int idReceta, String nuevoTitulo) {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/receta/modificarTituloReceta?_method=PUT";

        // Crea el cuerpo de la solicitud en formato JSON
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("idReceta", idReceta);
            requestBody.put("titulo", nuevoTitulo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crea la solicitud POST
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta del servidor si es necesario
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud si es necesario
                    }
                });

        // Añade la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }

    // Método para modificar la descripción, dificultad y tiempo de cocción de la receta
    private void modificarDescripcionDificultadTiempo(int idReceta, String nuevaDescripcion, String nuevoTiempo, String nuevaDificultad) {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/receta/modificarDescripcionReceta?_method=PUT";

        // Crea el cuerpo de la solicitud en formato JSON
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("idReceta", idReceta);
            requestBody.put("descripcion", nuevaDescripcion);
            requestBody.put("tiempoCoccion", nuevoTiempo);
            requestBody.put("dificultad", nuevaDificultad);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crea la solicitud POST
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta del servidor si es necesario
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud si es necesario
                    }
                });

        // Añade la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }

    // Método para modificar los ingredientes de la receta
    private void modificarIngredientes(int idReceta, List<Ingrediente> nuevosIngredientes) {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/receta/modificarIngredientes?_method=PUT";

        // Crea el cuerpo de la solicitud en formato JSON
        JSONArray ingredientesArray = new JSONArray();
        for (Ingrediente ingrediente : nuevosIngredientes) {
            ingredientesArray.put(ingrediente.getNombre());
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("idReceta", idReceta);
            requestBody.put("ingredientes", ingredientesArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crea la solicitud POST
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta del servidor si es necesario
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud si es necesario
                    }
                });

        // Añade la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }

    // Método para modificar los pasos de la receta
    /*private void modificarPasos(int idReceta, List<Paso> nuevosPasos) {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/receta/modificarPasos?_method=PUT";

        // Crea el cuerpo de la solicitud en formato JSON
        JSONArray pasosArray = new JSONArray();
        for (Paso paso : nuevosPasos) {
            JSONObject pasoObject = new JSONObject();
            try {
                // Enviar solo la descripción del paso
                pasoObject.put("descripcion", paso.getDescripcion());
                pasosArray.put(pasoObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("idReceta", idReceta);
            requestBody.put("pasos", pasosArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crea la solicitud POST
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta del servidor si es necesario
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud si es necesario
                    }
                });

        // Añade la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }*/
    /*private void modificarPasos(int idReceta, List<String> nuevosPasos) {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/receta/modificarPasos?_method=PUT";

        // Crea el cuerpo de la solicitud en formato JSON
        JSONArray pasosArray = new JSONArray(nuevosPasos);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("idReceta", idReceta);
            requestBody.put("pasos", pasosArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crea la solicitud POST
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta del servidor si es necesario
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud si es necesario
                    }
                });

        // Añade la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }*/
    private void modificarPasos(int idReceta, List<String> nuevosPasos) {
        String pc_ip = getResources().getString(R.string.pc_ip);
        String url = "http://" + pc_ip + ":3000/receta/modificarPasos";

        // Crea el cuerpo de la solicitud en formato JSON
        JSONArray pasosArray = new JSONArray(nuevosPasos);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("idReceta", idReceta);
            requestBody.put("pasos", pasosArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crea la solicitud PUT
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta del servidor si es necesario
                        Log.d("Response", "Response from server: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud si es necesario
                        Log.e("Error", "Error in request: " + error.toString());
                    }
                });

        // Añade la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }
}

