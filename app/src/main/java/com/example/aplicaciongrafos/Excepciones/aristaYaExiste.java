package com.example.aplicaciongrafos.Excepciones;

public class aristaYaExiste extends Exception{
    public aristaYaExiste(){
        super("Arista que quiere insertar ya existe");
    }
    public aristaYaExiste(String message){
        super(message);
    }
}

