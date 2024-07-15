package com.example.aplicaciongrafos.Grafos;

import com.example.aplicaciongrafos.Excepciones.AristaNoExiste;
import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;
import com.example.aplicaciongrafos.Excepciones.ExcepcionesNroVerticeInvalido;
import com.example.aplicaciongrafos.Excepciones.aristaYaExiste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grafo {
    final List<List<Integer>> listaDeAdyacencias;

    public Grafo() {
        this.listaDeAdyacencias = new ArrayList<>();
    }

    public Grafo(int nroDeVertices) throws ExcepcionesNroVerticeInvalido {
        if (nroDeVertices < 0) {
            throw new ExcepcionesNroVerticeInvalido();
        }
        this.listaDeAdyacencias = new ArrayList<>();
        for (int i = 0; i < nroDeVertices; i++) {
            this.insertarVertice();
        }
    }
    public int cantidadDeVertices() {
        return this.listaDeAdyacencias.size();
    }

    public void insertarVertice() {
        List<Integer> adyacenciasDelNuevoVertice = new ArrayList<>();
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
        List<Integer> adyacentesDelOrigen = this.listaDeAdyacencias.get(posDeVerticeOrigen);
        return adyacentesDelOrigen.contains(posDeVerticeDestino);
    }

    public void insertarAristas(int posdeVerticeOrigen, int posDeVerticeDestino) throws aristaYaExiste, ExcepcionPosicionDelVerticeInvalida {
        if (this.existeAdyacencia(posdeVerticeOrigen, posDeVerticeDestino)) {
            throw new aristaYaExiste();
        }
        List<Integer> adyacentesDelOrigen = this.listaDeAdyacencias.get(posdeVerticeOrigen);
        adyacentesDelOrigen.add(posDeVerticeDestino);
        Collections.sort(adyacentesDelOrigen);
        if (posdeVerticeOrigen != posDeVerticeDestino) {
            List<Integer> adyacentesDelDestino = this.listaDeAdyacencias.get(posDeVerticeDestino);
            adyacentesDelDestino.add(posdeVerticeOrigen);
            Collections.sort(adyacentesDelDestino);
        }
    }

    public int gradoDelVertice(int posDeVertice) throws ExcepcionPosicionDelVerticeInvalida {
        validarVertice(posDeVertice);
        List<Integer> adyacentesDelVertice = listaDeAdyacencias.get(posDeVertice);
        return adyacentesDelVertice.size();
    }

    public Iterable<Integer> adyacentesDelVertice(int posDeVertice) throws ExcepcionPosicionDelVerticeInvalida {
        validarVertice(posDeVertice);
        List<Integer> adyacentesDelVertice = this.listaDeAdyacencias.get(posDeVertice);
        List<Integer> copiaAdyacencias = new ArrayList<>();
        for (Integer adyacente : adyacentesDelVertice) {
            copiaAdyacencias.add(adyacente);
        }
        return (Iterable) copiaAdyacencias;
    }

    public void eliminarVertice(int posDeVertice) throws ExcepcionPosicionDelVerticeInvalida {
        validarVertice(posDeVertice);
        this.listaDeAdyacencias.remove(posDeVertice);
        for (List<Integer> adyacentesDeUnVertice : this.listaDeAdyacencias) {
            int posDeLaAdyacencia = adyacentesDeUnVertice.indexOf(posDeVertice);
            if (posDeLaAdyacencia>=0){
                adyacentesDeUnVertice.remove(posDeLaAdyacencia);
            }
            for (int i=0;i<adyacentesDeUnVertice.size();i++){
                int elementoVertice=adyacentesDeUnVertice.get(i);
                if(elementoVertice>posDeVertice){
                    elementoVertice--;
                    adyacentesDeUnVertice.set(i,elementoVertice);
                }
            }
        }
    }
    public void eliminarArista(int posDeVerticeOrigen, int posDeVerticeDestino) throws ExcepcionPosicionDelVerticeInvalida, AristaNoExiste {
        this.validarVertice(posDeVerticeOrigen);
        this.validarVertice(posDeVerticeDestino);

        List<Integer> adyacentesDelOrigen = this.listaDeAdyacencias.get(posDeVerticeOrigen);
        List<Integer> adyacentesDelDestino = this.listaDeAdyacencias.get(posDeVerticeDestino);

        if (adyacentesDelOrigen.contains(posDeVerticeDestino)) {
            adyacentesDelOrigen.remove(Integer.valueOf(posDeVerticeDestino));
        }

        if (adyacentesDelDestino.contains(posDeVerticeOrigen)) {
            adyacentesDelDestino.remove(Integer.valueOf(posDeVerticeOrigen));
        }
    }

}

