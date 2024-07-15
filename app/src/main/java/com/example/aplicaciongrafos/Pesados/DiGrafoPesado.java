package com.example.aplicaciongrafos.Pesados;

import com.example.aplicaciongrafos.Excepciones.AristaNoExiste;
import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;
import com.example.aplicaciongrafos.Excepciones.ExcepcionesNroVerticeInvalido;
import com.example.aplicaciongrafos.Excepciones.aristaYaExiste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiGrafoPesado {
    private List<List<AdyacenteConPeso>> listaDeAdyacencias;

    public DiGrafoPesado() {
        this.listaDeAdyacencias = new ArrayList<>();
    }

    public DiGrafoPesado(int nroDeVertices) throws ExcepcionesNroVerticeInvalido {
        if (nroDeVertices < 0) {
            throw new ExcepcionesNroVerticeInvalido();
        }
        this.listaDeAdyacencias = new ArrayList<>();
        for (int i = 0; i < nroDeVertices; i++) {
            this.insertarVertice();
        }
    }

    public void insertarVertice() {
        List<AdyacenteConPeso> adyacenciasDelNuevoVertice = new ArrayList<>();
        this.listaDeAdyacencias.add(adyacenciasDelNuevoVertice);
    }

    public void validarVertice(int posDelVertice) throws ExcepcionPosicionDelVerticeInvalida {
        if (posDelVertice < 0 || posDelVertice >= this.listaDeAdyacencias.size()) {
            throw new ExcepcionPosicionDelVerticeInvalida();
        }
    }

    public boolean existeAdyacencia(int posDeVerticeOrigen, int posDeVerticeDestino) throws ExcepcionPosicionDelVerticeInvalida {
        this.validarVertice(posDeVerticeOrigen);
        this.validarVertice(posDeVerticeDestino);
        List<AdyacenteConPeso> adyacentesDelOrigen = this.listaDeAdyacencias.get(posDeVerticeOrigen);
        for (AdyacenteConPeso adyacente : adyacentesDelOrigen) {
            if (adyacente.getIndiceVertice() == posDeVerticeDestino) {
                return true;
            }
        }
        return false;
    }

    public void insertarArista(int posDeVerticeOrigen, int posDeVerticeDestino, double peso) throws aristaYaExiste, ExcepcionPosicionDelVerticeInvalida {
        if (this.existeAdyacencia(posDeVerticeOrigen, posDeVerticeDestino)) {
            throw new aristaYaExiste();
        }
        List<AdyacenteConPeso> adyacentesDelOrigen = this.listaDeAdyacencias.get(posDeVerticeOrigen);
        adyacentesDelOrigen.add(new AdyacenteConPeso(posDeVerticeDestino, peso));
        Collections.sort(adyacentesDelOrigen);
    }

    public int cantidadDeVertices() {
        return this.listaDeAdyacencias.size();
    }

    public int gradoDelVertice(int posDeVertice) throws ExcepcionPosicionDelVerticeInvalida {
        validarVertice(posDeVertice);
        List<AdyacenteConPeso> adyacentesDelVertice = listaDeAdyacencias.get(posDeVertice);
        return adyacentesDelVertice.size();
    }

    public Iterable<AdyacenteConPeso> adyacentesDelVertice(int posDeVertice) throws ExcepcionPosicionDelVerticeInvalida {
        validarVertice(posDeVertice);
        List<AdyacenteConPeso> adyacentesDelVertice = this.listaDeAdyacencias.get(posDeVertice);
        List<AdyacenteConPeso> copiaAdyacencias = new ArrayList<>(adyacentesDelVertice);
        return copiaAdyacencias;
    }

    public void eliminarVertice(int posDeVertice) throws ExcepcionPosicionDelVerticeInvalida {
        validarVertice(posDeVertice);
        this.listaDeAdyacencias.remove(posDeVertice);
        for (List<AdyacenteConPeso> adyacentesDeUnVertice : this.listaDeAdyacencias) {
            int posDeLaAdyacencia = -1;
            for (int i = 0; i < adyacentesDeUnVertice.size(); i++) {
                if (adyacentesDeUnVertice.get(i).getIndiceVertice() == posDeVertice) {
                    posDeLaAdyacencia = i;
                    break;
                }
            }
            if (posDeLaAdyacencia >= 0) {
                adyacentesDeUnVertice.remove(posDeLaAdyacencia);
            }
            for (int i = 0; i < adyacentesDeUnVertice.size(); i++) {
                AdyacenteConPeso adyacente = adyacentesDeUnVertice.get(i);
                if (adyacente.getIndiceVertice() > posDeVertice) {
                    adyacente.setIndiceVertice(adyacente.getIndiceVertice() - 1);
                }
            }
        }
    }

    public void eliminarArista(int posDeVerticeOrigen, int posDeVerticeDestino) throws ExcepcionPosicionDelVerticeInvalida, AristaNoExiste {
        this.validarVertice(posDeVerticeOrigen);
        this.validarVertice(posDeVerticeDestino);

        List<AdyacenteConPeso> adyacentesDelOrigen = this.listaDeAdyacencias.get(posDeVerticeOrigen);
        AdyacenteConPeso adyacente = null;
        for (AdyacenteConPeso ady : adyacentesDelOrigen) {
            if (ady.getIndiceVertice() == posDeVerticeDestino) {
                adyacente = ady;
                break;
            }
        }
        if (adyacente == null) {
            throw new AristaNoExiste();
        }

        adyacentesDelOrigen.remove(adyacente);
    }

    public int cantidadDeAristas() {
        int cantAristas = 0;
        for (int i = 0; i < this.listaDeAdyacencias.size(); i++) {
            List<AdyacenteConPeso> adyacentesDelVertice = this.listaDeAdyacencias.get(i);
            cantAristas += adyacentesDelVertice.size();
        }
        return cantAristas;
    }
}
