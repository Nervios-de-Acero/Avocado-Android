package com.example.proyectoavocado.reciclesAdaptadores;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoavocado.R;
import com.example.proyectoavocado.controllers.Paso;

import java.util.List;

public class PasoViewAdapter extends RecyclerView.Adapter<PasoViewAdapter.PasoViewHolder> {
    private List<String> pasos;

    public PasoViewAdapter(List<String> pasos) {
        this.pasos = pasos;
    }

    @NonNull
    @Override
    public PasoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.paso_view, parent, false);
        return new PasoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PasoViewHolder holder, int position) {
        String descripcionPaso = pasos.get(position);
        holder.setDescripcionPaso(descripcionPaso);
    }

    @Override
    public int getItemCount() {
        return pasos.size();
    }

    static class PasoViewHolder extends RecyclerView.ViewHolder {
        TextView descripcionPaso;

        PasoViewHolder(View itemView) {
            super(itemView);
            descripcionPaso = itemView.findViewById(R.id.descripcion_paso);
        }

        public void setDescripcionPaso(String descripcion) {
            descripcionPaso.setText(descripcion);
        }
    }

    /*@Override
    public int getItemCount() {
        return pasos.size();
    }

    static class PasoViewHolder extends RecyclerView.ViewHolder {
        TextView tituloPaso;
        TextView descripcionPaso;

        PasoViewHolder(View itemView) {
            super(itemView);
            tituloPaso = itemView.findViewById(R.id.titulo_paso);
            descripcionPaso = itemView.findViewById(R.id.descripcion_paso);
        }
    }*/
}
