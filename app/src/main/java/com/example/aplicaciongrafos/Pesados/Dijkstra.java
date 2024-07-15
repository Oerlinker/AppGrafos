package com.example.aplicaciongrafos.Pesados;

import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;
import com.example.aplicaciongrafos.Grafos.ControlMarcado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
    private GrafoPesado grafo;

    public Dijkstra(GrafoPesado grafo) {
        this.grafo = grafo;
    }
    public double[] dijkstra(GrafoPesado grafo, int origen) throws ExcepcionPosicionDelVerticeInvalida {
        int n = grafo.cantidadDeVertices();
        double[] dist = new double[n];
        ControlMarcado controlMarcado = new ControlMarcado(n);
        PriorityQueue<AdyacenteConPeso> pq = new PriorityQueue<>();

        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[origen] = 0;
        pq.add(new AdyacenteConPeso(origen, 0));

        while (!pq.isEmpty()) {
            int u = pq.poll().getIndiceVertice();
            if (controlMarcado.estaMarcado(u)) {
                continue;
            }
            controlMarcado.marcarVertice(u);

            for (AdyacenteConPeso adyacente : grafo.adyacentesDelVertice(u)) {
                int v = adyacente.getIndiceVertice();
                double peso = adyacente.getPeso();
                if (!controlMarcado.estaMarcado(v) && dist[u] + peso < dist[v]) {
                    dist[v] = dist[u] + peso;
                    pq.add(new AdyacenteConPeso(v, dist[v]));
                }
            }
        }

        return dist;
    }

    public List<Integer> encontrarRutaMasCorta(int origen, int destino) throws ExcepcionPosicionDelVerticeInvalida {
        int n = grafo.cantidadDeVertices();

        if (n == 0) {
            throw new IllegalArgumentException("El grafo está vacío");
        }

        double[] distancias = new double[n];
        int[] predecesores = new int[n];
        ControlMarcado controlMarcado = new ControlMarcado(n);
        PriorityQueue<AdyacenteConPeso> pq = new PriorityQueue<>();

        Arrays.fill(distancias, Double.POSITIVE_INFINITY);
        distancias[origen] = 0;
        Arrays.fill(predecesores, -1);
        pq.add(new AdyacenteConPeso(origen, 0));

        while (!pq.isEmpty()) {
            int u = pq.poll().getIndiceVertice();
            if (controlMarcado.estaMarcado(u)) {
                continue;
            }
            controlMarcado.marcarVertice(u);

            for (AdyacenteConPeso adyacente : grafo.adyacentesDelVertice(u)) {
                int v = adyacente.getIndiceVertice();
                double peso = adyacente.getPeso();
                if (!controlMarcado.estaMarcado(v) && distancias[u] + peso < distancias[v]) {
                    distancias[v] = distancias[u] + peso;
                    predecesores[v] = u;
                    pq.add(new AdyacenteConPeso(v, distancias[v]));
                }
            }
        }

        // construimos la ruta
        List<Integer> ruta = new ArrayList<>();
        for (int at = destino; at != -1; at = predecesores[at]) {
            ruta.add(at);
        }
        Collections.reverse(ruta);

        // verificar la valid
        if (ruta.get(0) != origen) {
            throw new IllegalStateException("No se encontró una ruta válida desde el origen al destino");
        }

        return ruta;
    }
}



