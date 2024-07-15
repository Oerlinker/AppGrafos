package com.example.aplicaciongrafos.Pesados;

import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;
import com.example.aplicaciongrafos.Grafos.ControlMarcado;

import java.util.PriorityQueue;

public class Prim {
    public double prim(GrafoPesado grafo) throws ExcepcionPosicionDelVerticeInvalida {
        int n = grafo.cantidadDeVertices();
        ControlMarcado controlMarcado = new ControlMarcado(n); // Usamos ControlMarcado
        PriorityQueue<AdyacenteConPeso> pq = new PriorityQueue<>();
        double pesoTotal = 0;

        pq.add(new AdyacenteConPeso(0, 0));

        while (!pq.isEmpty()) {
            AdyacenteConPeso adyacente = pq.poll();
            int u = adyacente.getIndiceVertice();
            if (controlMarcado.estaMarcado(u)) {
                continue;
            }
            controlMarcado.marcarVertice(u);
            pesoTotal += adyacente.getPeso();

            for (AdyacenteConPeso ady : grafo.adyacentesDelVertice(u)) {
                if (!controlMarcado.estaMarcado(ady.getIndiceVertice())) {
                    pq.add(ady);
                }
            }
        }

        return pesoTotal;
    }
}
