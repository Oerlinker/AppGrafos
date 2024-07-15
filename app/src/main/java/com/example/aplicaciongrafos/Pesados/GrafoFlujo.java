package com.example.aplicaciongrafos.Pesados;

import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;
import com.example.aplicaciongrafos.Excepciones.ExcepcionesNroVerticeInvalido;
import com.example.aplicaciongrafos.Excepciones.aristaYaExiste;

import java.util.HashMap;
import java.util.Map;

public class GrafoFlujo extends GrafoPesado{
    private Map<Integer, Map<Integer, Double>> capacidadResidual;

    public GrafoFlujo() {
        super();
        capacidadResidual = new HashMap<>();
    }

    public GrafoFlujo(int nroDeVertices) throws ExcepcionesNroVerticeInvalido {
        super(nroDeVertices);
        capacidadResidual = new HashMap<>();
        for (int i = 0; i < nroDeVertices; i++) {
            capacidadResidual.put(i, new HashMap<>());
        }
    }

    @Override
    public void insertarArista(int posDeVerticeOrigen, int posDeVerticeDestino, double peso) throws aristaYaExiste, ExcepcionPosicionDelVerticeInvalida {
        if (this.existeAdyacencia(posDeVerticeOrigen, posDeVerticeDestino)) {
            // Si ya existe la adyacencia, solo actualizamos la capacidad residual
            double capacidadActual = capacidadResidual.get(posDeVerticeOrigen).get(posDeVerticeDestino);
            capacidadResidual.get(posDeVerticeOrigen).put(posDeVerticeDestino, capacidadActual + peso);
        } else {
            super.insertarArista(posDeVerticeOrigen, posDeVerticeDestino, peso);
            capacidadResidual.get(posDeVerticeOrigen).put(posDeVerticeDestino, peso);
            capacidadResidual.get(posDeVerticeDestino).put(posDeVerticeOrigen, 0.0);
        }
    }

    public double getCapacidadResidual(int u, int v) {
        return capacidadResidual.get(u).get(v);
    }

    public void actualizarCapacidadResidual(int u, int v, double flujo) {
        capacidadResidual.get(u).put(v, capacidadResidual.get(u).get(v) + flujo);
    }
}
