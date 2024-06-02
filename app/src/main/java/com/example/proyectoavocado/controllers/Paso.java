package com.example.proyectoavocado.controllers;

public class Paso {
    private Integer idPaso;
    private Integer idReceta;
    private String titulo;
    private String descripcion;

    // Constructor con solo descripcion
    public Paso(String descripcion) {
        this.descripcion = descripcion;
    }

    // Constructor completo
    public Paso(int idPaso, String titulo, String descripcion) {
        this.idPaso = idPaso;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

 /*   public Paso(int idPaso, String titulo, String descripcion){
        this.idPaso = idPaso;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Paso(String titulo, String descripcion ) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }*/

    public int getIdPaso() {
        return idPaso;
    }

    public void setIdPaso(int idPaso) {
        this.idPaso = idPaso;
    }

    public Integer getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String toString() {
        return "Receta{" +
                "idPaso =" + idPaso +
                ", idReceta='" + idReceta + '\'' +
                ", titulo ='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}

