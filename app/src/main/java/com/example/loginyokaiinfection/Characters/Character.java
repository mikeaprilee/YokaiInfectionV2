package com.example.loginyokaiinfection.Characters;

import java.util.Locale;

/**
 * Modelo de un personaje yokai.
 * Acepta el formato antiguo "Nombre - Tipo" para conservar compatibilidad con Datos.
 */
public class Character {
    private int id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private String imagen;

    public Character(String nombre, String descripcion) {
        this(0, nombre, descripcion);
    }

    public Character(int id, String nombre, String descripcion) {
        this(id, cleanName(nombre), extractType(nombre), descripcion,
                makeImageName(cleanName(nombre)));
    }

    public Character(int id, String nombre, String tipo,
                     String descripcion, String imagen) {
        this.id = id;
        this.nombre = nombre == null ? "" : nombre.trim();
        this.tipo = tipo == null ? "Yokai" : tipo.trim();
        this.descripcion = descripcion == null ? "" : descripcion.trim();
        this.imagen = imagen == null ? makeImageName(this.nombre) : imagen.trim();
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
        this.nombre = cleanName(nombre);
        this.imagen = makeImageName(this.nombre);
    }

    public void setTipo(String tipo) {
        this.tipo = tipo == null ? "Yokai" : tipo.trim();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }

    public void setImagen(String imagen) {
        this.imagen = imagen == null ? makeImageName(nombre) : imagen.trim();
    }

    @Override
    public String toString() {
        return nombre + " - " + tipo;
    }

    private static String cleanName(String fullName) {
        if (fullName == null) {
            return "";
        }
        int separator = fullName.indexOf(" - ");
        return (separator >= 0 ? fullName.substring(0, separator) : fullName).trim();
    }

    private static String extractType(String fullName) {
        if (fullName == null) {
            return "Yokai";
        }
        int separator = fullName.indexOf(" - ");
        return separator >= 0
                ? fullName.substring(separator + 3).trim()
                : "Yokai";
    }

    private static String makeImageName(String name) {
        return name == null ? "" : name.trim().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");
    }
}
