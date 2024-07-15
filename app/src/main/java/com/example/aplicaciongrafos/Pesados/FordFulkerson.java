package com.example.aplicaciongrafos.Pesados;

import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;
import com.example.aplicaciongrafos.Excepciones.aristaYaExiste;
import com.example.aplicaciongrafos.Grafos.ControlMarcado;

import java.util.LinkedList;
import java.util.Queue;

public class FordFulkerson {
    private ControlMarcado controlMarcado;
    private int[] caminoAumentante;
    private double flujoMaximo;

    public FordFulkerson(GrafoFlujo grafo, int fuente, int sumidero) throws ExcepcionPosicionDelVerticeInvalida, aristaYaExiste {
        flujoMaximo = 0;
        while (hayCaminoAumentante(grafo, fuente, sumidero)) {
            double flujo = Double.POSITIVE_INFINITY;

            for (int v = sumidero; v != fuente; v = caminoAumentante[v]) {
                int u = caminoAumentante[v];
                flujo = Math.min(flujo, grafo.getCapacidadResidual(u, v));
            }

            for (int v = sumidero; v != fuente; v = caminoAumentante[v]) {
                int u = caminoAumentante[v];
                grafo.actualizarCapacidadResidual(u, v, -flujo);
                grafo.actualizarCapacidadResidual(v, u, flujo);
            }

            flujoMaximo += flujo;
        }
    }

    private boolean hayCaminoAumentante(GrafoFlujo grafo, int fuente, int sumidero) throws ExcepcionPosicionDelVerticeInvalida {
        controlMarcado = new ControlMarcado(grafo.cantidadDeVertices());
        caminoAumentante = new int[grafo.cantidadDeVertices()];

        Queue<Integer> cola = new LinkedList<>();
        cola.add(fuente);
        controlMarcado.marcarVertice(fuente);

        while (!cola.isEmpty() && !controlMarcado.estaMarcado(sumidero)) {
            int u = cola.poll();

            for (AdyacenteConPeso adyacente : grafo.adyacentesDelVertice(u)) {
                int v = adyacente.getIndiceVertice();
                double capacidadResidual = grafo.getCapacidadResidual(u, v);

                if (!controlMarcado.estaMarcado(v) && capacidadResidual > 0) {
                    caminoAumentante[v] = u;
                    controlMarcado.marcarVertice(v);
                    cola.add(v);
                }
            }
        }

        return controlMarcado.estaMarcado(sumidero);
    }

    public double getFlujoMaximo() {
        return flujoMaximo;
    }
}