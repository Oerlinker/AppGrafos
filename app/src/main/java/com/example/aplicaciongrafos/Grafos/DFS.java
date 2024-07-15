package com.example.aplicaciongrafos.Grafos;

import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;

import java.util.ArrayList;
import java.util.List;

public class DFS {
    private final Grafo elGrafo;
    private final ControlMarcado marcados;
    private final List<Integer> recorrido;

    public DFS(Grafo unGrafo,int posVerticeInicial) throws ExcepcionPosicionDelVerticeInvalida{
        this.elGrafo=unGrafo;
        this.recorrido=new ArrayList<>();
        this.marcados=new ControlMarcado(
                elGrafo.cantidadDeVertices());
        this.ejecutarDFS(posVerticeInicial);
    }
    public void ejecutarDFS(int posDeVerticeAct) throws ExcepcionPosicionDelVerticeInvalida {
        this.elGrafo.validarVertice(posDeVerticeAct);
        this.recorrido.add(posDeVerticeAct);
        this.marcados.marcarVertice(posDeVerticeAct);
        Iterable<Integer>adysDelVerticeAct=this.elGrafo.
                adyacentesDelVertice(posDeVerticeAct);
        for (Integer posDeAdy:adysDelVerticeAct){
            if (!this.marcados.estaMarcado(posDeAdy)){
                ejecutarDFS(posDeAdy);
            }
        }
    }
    public Iterable<Integer> getRecorrido(){
        return this.recorrido;
    }
    public boolean llegoATodos(){
        return this.marcados.estanTodosMarcados();
    }


}
