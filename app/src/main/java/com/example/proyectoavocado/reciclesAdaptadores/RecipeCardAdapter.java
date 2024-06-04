package com.example.proyectoavocado.reciclesAdaptadores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoavocado.R;
import com.example.proyectoavocado.controllers.Receta;
import com.example.proyectoavocado.VistaDetalladaActivity;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ViewHolder> {
    private List<Receta> recetas;
    private Context context;

    //La lista que se va modificar
    List<Receta> listaOriginal;

    public RecipeCardAdapter(List<Receta> recetas, Context context) {
        this.recetas = recetas;
        this.context = context;
        //Le cargo recetas
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(recetas);
    }

    @NonNull
    @Override
    public RecipeCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de la tarjeta receta_feed_card.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receta_feed_card, parent, false);
        return new ViewHolder(view);
    }
    //Funcion de Filtrado
    public void filtrado(String searchView) {
        int longitud = searchView.length();
        if (longitud == 0) {
            recetas.clear();
            recetas.addAll(listaOriginal);
        } else {
            List<Receta> filteredList = new ArrayList<>();
            for (Receta receta : listaOriginal) {
                if (receta.getTitulo().toLowerCase().contains(searchView.toLowerCase())) {
                    filteredList.add(receta);
                }
            }
            recetas.clear();
            recetas.addAll(filteredList);
        }

        if (recetas.isEmpty()) {
            Toast.makeText(context, "No se encontraron recetas", Toast.LENGTH_SHORT).show();
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCardAdapter.ViewHolder holder, int position) {
        Receta receta = recetas.get(position);

        // Configurar las vistas de la tarjeta con datos de la receta
        holder.titulo_receta.setText(receta.getTitulo());
        holder.descripcion_receta.setText(receta.getDescripcion());
        holder.usuario_id_nombre.setText(receta.getCreadoPor());
        String imagen = receta.getImagen();
        if(imagen != null && imagen != "null") {
            Picasso.get()
                    .load(imagen)
                    .fit()
                    .centerInside()
                    .into(holder.imagen_comida);
        } else {
            String placeholder = "https://www.cronista.com/files/image/725/725524/6580a8bde93f1.jpg";
            Picasso.get()
                    .load(placeholder)
                    .fit()
                    .centerInside()
                    .into(holder.imagen_comida);
        }

        // Configurar el clic de la tarjeta
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el ID de la receta seleccionada como String
                Integer idDeLaReceta = receta.getIdReceta();
                Intent intent = new Intent(context, VistaDetalladaActivity.class);
                intent.putExtra("receta_id", idDeLaReceta);
                intent.putExtra("idReceta", receta.getIdReceta());
                context.startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen_comida;
        TextView usuario_id_nombre;
        TextView titulo_receta;
        TextView descripcion_receta;

        public ViewHolder(View itemView) {
            super(itemView);
            // Inicializar las vistas
            imagen_comida = itemView.findViewById(R.id.imagen_comida);
            usuario_id_nombre = itemView.findViewById(R.id.usuario_id_nombre);
            titulo_receta = itemView.findViewById(R.id.titulo_receta);
            descripcion_receta = itemView.findViewById(R.id.descripcion_receta);
        };
    }
}