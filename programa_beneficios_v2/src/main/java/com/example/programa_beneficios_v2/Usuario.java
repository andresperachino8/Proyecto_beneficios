package com.example.programa_beneficios_v2;

public class Usuario {
    private String nombre_usuario;
    private String clave;

    public Usuario(){
        nombre_usuario="";
        clave="";
    }

    public Usuario(String nombre_usuario, String clave ){
        this.nombre_usuario = nombre_usuario;
        this.clave = clave;
    }

    public void setNombre(String nombre_usuario){
        this.nombre_usuario=nombre_usuario;
    }

    public String getNombre(){
        return nombre_usuario;
    }

    public void setClave(String clave){
        this.clave=clave;
    }

    public String getClave(){
        return clave;
    }    
}
