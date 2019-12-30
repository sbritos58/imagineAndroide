package com.santi.imagine.models;

public class Usuarios {

    private String nombreOrganizacion;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private String fecha;
    private String contraseña;

    public Usuarios() {
    }

    public Usuarios(String nombreOrganizacion, String nombreCompleto, String email, String telefono, String fecha, String contraseña) {
        this.nombreOrganizacion = nombreOrganizacion;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.telefono = telefono;
        this.fecha = fecha;
        this.contraseña = contraseña;
    }

    public String getNombreOrganizacion() {
        return nombreOrganizacion;
    }

    public void setNombreOrganizacion(String nombreOrganizacion) {
        this.nombreOrganizacion = nombreOrganizacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
