package com.example.aplicaciongrafos.Grafos;

import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFS {
    private final Grafo elGrafo;
    private final ControlMarcado marcados;
    private final List<Integer> recorrido;

    public BFS(Grafo unGrafo, int posVerticeInicial) throws ExcepcionPosicionDelVerticeInvalida {
        unGrafo.validarVertice(posVerticeInicial);
        this.elGrafo = unGrafo;
        this.recorrido = new ArrayList<>();
        this.marcados = new ControlMarcado(elGrafo.cantidadDeVertices());
        this.ejecutarBFS(posVerticeInicial);
    }

    public void ejecutarBFS(int posDeVertice) throws ExcepcionPosicionDelVerticeInvalida {
        this.elGrafo.validarVertice(posDeVertice);
        Queue<Integer> colaDePosVertice = new LinkedList<>();
        colaDePosVertice.offer(posDeVertice);
        this.marcados.marcarVertice(posDeVertice);
        do {
            int posDeVerticeAct = colaDePosVertice.poll();
            Iterable<Integer> adysDeVerticeAct = this.elGrafo.adyacentesDelVertice(posDeVerticeAct);
            this.recorrido.add(posDeVerticeAct);
            for (Integer posDeAdy : adysDeVerticeAct) {
                if (!this.marcados.estaMarcado(posDeAdy)) {
                    colaDePosVertice.offer(posDeAdy);
                    this.marcados.marcarVertice(posDeAdy);
                }
            }
        } while (!colaDePosVertice.isEmpty());
    }

    public List<Integer> getRecorrido() {
        return this.recorrido;
    }
}
