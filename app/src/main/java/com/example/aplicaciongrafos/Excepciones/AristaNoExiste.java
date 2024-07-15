package com.example.aplicaciongrafos.Excepciones;

public class AristaNoExiste extends Exception {
    public AristaNoExiste(){
        super("Arista que quiere eliminar no existe");
    }
    public AristaNoExiste(String message){
        super(message);
    }
}

