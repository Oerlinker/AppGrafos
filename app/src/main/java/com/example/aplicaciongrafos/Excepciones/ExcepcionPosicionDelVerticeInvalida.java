package com.example.aplicaciongrafos.Excepciones;

public class ExcepcionPosicionDelVerticeInvalida extends Exception{
    public ExcepcionPosicionDelVerticeInvalida (){
        super("posicion del vertice invalida");
    }
    public ExcepcionPosicionDelVerticeInvalida (String message){
        super(message);
    }
}

