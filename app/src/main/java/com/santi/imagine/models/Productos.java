package com.santi.imagine.models;

public class Productos {
    private String producto;
    private String descripcion;
    private String cantidad;
    private String pais;
    private String ubicacion;
    private String url;
    private String latitude;
    private String longitude;
    private String tokenUsuario;

    public Productos() {
    }

    public Productos(String producto, String descripcion, String cantidad, String pais,String ubicacion, String url, String tokenUsuario, String latitude, String longitude) {
        this.descripcion = descripcion;
        this.producto = producto;
        this.cantidad = cantidad;
        this.pais = pais;
        this.ubicacion = ubicacion;
        this.url = url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tokenUsuario = tokenUsuario;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getTokenUsuario() {
        return tokenUsuario;
    }

    public void setTokenUsuario(String tokenUsuario) {
        this.tokenUsuario = tokenUsuario;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
        public String getDescripcion () {
            return descripcion;
        }

        public void setDescripcion (String descripcion){
            this.descripcion = descripcion;
        }


        public String getLongitude () {
            return longitude;
        }

        public void setLongitude (String longitude){
            this.longitude = longitude;
        }

    }
