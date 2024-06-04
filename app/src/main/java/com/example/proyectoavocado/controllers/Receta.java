package com.example.proyectoavocado.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

public class Receta implements Serializable {
    private Integer idReceta;
    private String titulo;
    private String imagen;
    private String descripcion;
    private String creadoPor;
    private String tiempoCoccion;
    private String dificultad;
    private Date fechaCreacion;
    private Date fechaActualizacion;
    private List<String> categorias;
    private List<Ingrediente> ingredientes;
    private List<String> pasos;

    // Constructor vacío necesario para deserialización
    public Receta() {
    }

    // Constructor completo
    public Receta(Integer idReceta, String titulo, String imagen, String descripcion, String creadoPor, String tiempoCoccion, String dificultad, Date fechaCreacion, Date fechaActualizacion, List<String> pasos, List<String> categorias, List<Ingrediente> ingredientes) {
        this.idReceta = idReceta;
        this.titulo = titulo;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.creadoPor = creadoPor;
        this.tiempoCoccion = tiempoCoccion;
        this.dificultad = dificultad;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
        this.pasos = pasos;
        this.categorias = categorias;
        this.ingredientes = ingredientes;
    }

    public Receta(Integer idReceta, String titulo, String creadoPor, String descripcion, String imagen) {
        this.idReceta = idReceta;
        this.titulo = titulo;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.creadoPor = creadoPor;
    }

    public Receta(Integer idReceta, String titulo, String descripcion, String tiempoCoccion, String dificultad, List<Ingrediente> ingredientes, List<String> pasos) {
        this.idReceta = idReceta;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.creadoPor = creadoPor;
        this.tiempoCoccion = tiempoCoccion;
        this.dificultad = dificultad;
        this.pasos = pasos;
        this.ingredientes = ingredientes;
    }

    public Receta(int idReceta, String titulo, String imagen) {
        this.idReceta = idReceta;
        this.titulo = titulo;
        this.imagen = imagen;
    }


    // Otros constructores aquí...

    // Métodos Getter y Setter
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }

    public String getTiempoCoccion() {
        return tiempoCoccion;
    }

    public void setTiempoCoccion(String tiempoCoccion) {
        this.tiempoCoccion = tiempoCoccion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<String> getPasos() {
        return pasos;
    }

    public void setPasos(List<String> pasos) {
        this.pasos = pasos;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "idReceta=" + idReceta +
                ", titulo='" + titulo + '\'' +
                ", imagen='" + imagen + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", creadoPor='" + creadoPor + '\'' +
                ", tiempoCoccion='" + tiempoCoccion + '\'' +
                ", dificultad='" + dificultad + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                ", categorias=" + categorias +
                ", ingredientes=" + ingredientes +
                ", pasos=" + pasos +
                '}';
    }
}
