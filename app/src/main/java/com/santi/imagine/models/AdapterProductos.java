package com.santi.imagine.models;

public class AdapterProductos {


    private String producto;
    private String descripcion;
    private String cantidad;
    private String pais;
    private String ciudad;
    private String url;
    private String tokenUsuario;



    public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }




        public String getTokenUsuario() {
            return tokenUsuario;
        }

        public void setTokenUsuario(String tokenUsuario) {
            this.tokenUsuario = tokenUsuario;
        }


        public AdapterProductos() {
        }

        public AdapterProductos(String producto,String descripcion, String cantidad, String pais, String ciudad, String url, String tokenUsuario) {
            this.descripcion = descripcion;
            this.producto = producto;
            this.cantidad = cantidad;
            this.pais = pais;
            this.ciudad = ciudad;
            this.url = url;
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

        public String getCiudad() {
            return ciudad;
        }

        public void setCiudad(String ciudad) {
            this.ciudad = ciudad;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
