package com.example.proyectoavocado.reciclesAdaptadores;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoavocado.R;
import com.example.proyectoavocado.controllers.Paso;

import java.util.List;

public class PasosRecetaRecipeAdapter extends RecyclerView.Adapter<PasosRecetaRecipeAdapter.PasoViewHolder> {
    private List<String> pasos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public PasosRecetaRecipeAdapter(List<String> pasos, OnItemClickListener listener) {
        this.pasos = pasos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PasoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pasos_receta, parent, false);
        return new PasoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PasoViewHolder holder, int position) {
        String descripcionPaso = pasos.get(position);
        holder.tituloPaso.setText("Paso " + (position + 1)); // Establece el título del paso como "Paso X"
        holder.descripcionPaso.setText(descripcionPaso);

        holder.eliminarButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pasos.size();
    }

    public List<String> getPasos() {
        return pasos;
    }

    static class PasoViewHolder extends RecyclerView.ViewHolder {
        TextView tituloPaso;
        TextView descripcionPaso;
        ImageButton eliminarButton;

        PasoViewHolder(View itemView) {
            super(itemView);
            tituloPaso = itemView.findViewById(R.id.titulo_paso);
            descripcionPaso = itemView.findViewById(R.id.descripcion_pasoReceta);
            eliminarButton = itemView.findViewById(R.id.btn_eliminarPaso);
        }
    }
}
