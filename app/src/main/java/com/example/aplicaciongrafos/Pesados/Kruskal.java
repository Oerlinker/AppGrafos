package com.example.aplicaciongrafos.Pesados;

import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kruskal {
    private final GrafoPesado grafo;
    private final List<Arista> aristas;

    public Kruskal(GrafoPesado grafo) throws ExcepcionPosicionDelVerticeInvalida {
        this.grafo = grafo;
        this.aristas = new ArrayList<>();
        for (int i = 0; i < grafo.cantidadDeVertices(); i++) {
            for (AdyacenteConPeso adyacente : grafo.adyacentesDelVertice(i)) {
                aristas.add(new Arista(i, adyacente.getIndiceVertice(), adyacente.getPeso()));
            }
        }
    }

    public List<Arista> costoMinimo() {
        Collections.sort(aristas);
        UnionFind uf = new UnionFind(grafo.cantidadDeVertices());
        List<Arista> resultado = new ArrayList<>();

        for (Arista arista : aristas) {
            if (uf.find(arista.origen) != uf.find(arista.destino)) {
                uf.union(arista.origen, arista.destino);
                resultado.add(arista);
            }
        }

        return resultado;
    }

    private class Arista implements Comparable<Arista> {
        int origen;
        int destino;
        double peso;

        public Arista(int origen, int destino, double peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }

        @Override
        public int compareTo(Arista o) {
            return Double.compare(this.peso, o.peso);
        }
    }
}
