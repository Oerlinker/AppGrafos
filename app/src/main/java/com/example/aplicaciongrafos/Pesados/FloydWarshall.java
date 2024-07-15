package com.example.aplicaciongrafos.Pesados;

import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;

public class FloydWarshall {
    public double[][] algoritmoDeFloydWarshall(GrafoPesado grafo) throws ExcepcionPosicionDelVerticeInvalida {
        int n = grafo.cantidadDeVertices();
        double[][] dist = new double[n][n];

        // Inicialización de la matriz de distancias
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else if (grafo.existeAdyacencia(i, j)) {
                    for (AdyacenteConPeso adyacente : grafo.adyacentesDelVertice(i)) {
                        if (adyacente.getIndiceVertice() == j) {
                            dist[i][j] = adyacente.getPeso();
                        }
                    }
                } else {
                    dist[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }

        // Aplicación del algoritmo de Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }
}
