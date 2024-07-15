package com.example.aplicaciongrafos.Excepciones;

public class ExcepcionesNroVerticeInvalido extends Exception{
    public ExcepcionesNroVerticeInvalido(){
        super("cantidad de vertices no puede ser negativa");
    }
    public ExcepcionesNroVerticeInvalido(String message){
        super(message);
    }

}

