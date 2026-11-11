package com.example.loginyokaiinfection.Characters;

public class YokaiCharacter {

    private int id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private String imagen;

    public YokaiCharacter(
            String nombre,
            String tipo,
            String descripcion,
            String imagen) {

        this(0, nombre, tipo, descripcion, imagen);
    }

    public YokaiCharacter(
            int id,
            String nombre,
            String tipo,
            String descripcion,
            String imagen) {

        this.id = id;
        this.nombre = nombre == null ? "" : nombre.trim();
        this.tipo = tipo == null ? "Yokai" : tipo.trim();
        this.descripcion = descripcion == null ? "" : descripcion.trim();
        this.imagen = imagen == null ? "" : imagen.trim();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre == null ? "" : nombre.trim();
    }

    public void setTipo(String tipo) {
        this.tipo = tipo == null ? "Yokai" : tipo.trim();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }

    public void setImagen(String imagen) {
        this.imagen = imagen == null ? "" : imagen.trim();
    }

    @Override
    public String toString() {
        return nombre + " - " + tipo;
    }
}