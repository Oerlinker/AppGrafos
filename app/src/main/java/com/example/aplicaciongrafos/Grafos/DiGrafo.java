package com.example.aplicaciongrafos.Grafos;


import com.example.aplicaciongrafos.Excepciones.AristaNoExiste;
import com.example.aplicaciongrafos.Excepciones.ExcepcionPosicionDelVerticeInvalida;
import com.example.aplicaciongrafos.Excepciones.ExcepcionesNroVerticeInvalido;
import com.example.aplicaciongrafos.Excepciones.aristaYaExiste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DiGrafo extends Grafo {
    public DiGrafo() {
        super();
    }

    public DiGrafo(int nroDeVertices) throws ExcepcionesNroVerticeInvalido {
        super(nroDeVertices);
    }

    public void insertarArista(int posDeVerticeOrigen, int posDeVerticeDestino) throws aristaYaExiste, ExcepcionPosicionDelVerticeInvalida {
        if (this.existeAdyacencia(posDeVerticeOrigen, posDeVerticeDestino)) {
            throw new aristaYaExiste();
        }
        List<Integer> adyacentesDelOrigen = this.listaDeAdyacencias.get(posDeVerticeOrigen);
        adyacentesDelOrigen.add(posDeVerticeDestino);
        Collections.sort(adyacentesDelOrigen);
    }

    @Override
    public int gradoDelVertice(int posDeVertice) throws ExcepcionPosicionDelVerticeInvalida {
        return this.gradoDeSalidaDeVertice(posDeVertice);
    }

    @Override
    public void eliminarArista(int posDeVerticeOrigen, int posDeVerticeDestino) throws AristaNoExiste, ExcepcionPosicionDelVerticeInvalida {
        this.validarVertice(posDeVerticeOrigen);
        this.validarVertice(posDeVerticeDestino);

        List<Integer> adyacentesDelOrigen = this.listaDeAdyacencias.get(posDeVerticeOrigen);

        if (!adyacentesDelOrigen.contains(posDeVerticeDestino)) {
            throw new AristaNoExiste();
        }

        adyacentesDelOrigen.remove(Integer.valueOf(posDeVerticeDestino));
    }

    public int gradoDeSalidaDeVertice(int posDeVertice) throws ExcepcionPosicionDelVerticeInvalida {
        validarVertice(posDeVertice);
        List<Integer> adyacentesDelVertice = this.listaDeAdyacencias.get(posDeVertice);
        return adyacentesDelVertice.size();
    }

    public int gradoDeEntradaDeVertice(int posDeVertice) throws ExcepcionPosicionDelVerticeInvalida {
        validarVertice(posDeVertice);
        int gradoDeEntrada = 0;
        for (List<Integer> adyacentesDelVertice : this.listaDeAdyacencias) {
            if (adyacentesDelVertice.contains(posDeVertice)) {
                gradoDeEntrada++;
            }
        }
        return gradoDeEntrada;
    }

    // Método para realizar el ordenamiento topológico
    public List<Integer> ordenamientoTopologico() throws ExcepcionPosicionDelVerticeInvalida {
        int nroDeVertices = this.cantidadDeVertices();
        ControlMarcado marcados = new ControlMarcado(nroDeVertices);
        Stack<Integer> pila = new Stack<>();

        for (int i = 0; i < nroDeVertices; i++) {
            if (!marcados.estaMarcado(i)) {
                ordenamientoTopologicoUtil(i, marcados, pila);
            }
        }

        List<Integer> ordenTopologico = new ArrayList<>();
        while (!pila.isEmpty()) {
            ordenTopologico.add(pila.pop());
        }
        return ordenTopologico;
    }

    // Método auxiliar para el ordenamiento topológico
    private void ordenamientoTopologicoUtil(int v, ControlMarcado marcados, Stack<Integer> pila) throws ExcepcionPosicionDelVerticeInvalida {
        marcados.marcarVertice(v);
        for (int adyacente : this.listaDeAdyacencias.get(v)) {
            if (!marcados.estaMarcado(adyacente)) {
                ordenamientoTopologicoUtil(adyacente, marcados, pila);
            }
        }
        pila.push(v);
    }

    // Método para verificar si el grafo es acíclico utilizando DFS
    public boolean esAciclico() throws ExcepcionPosicionDelVerticeInvalida {
        int nroDeVertices = this.cantidadDeVertices();
        ControlMarcado visitados = new ControlMarcado(nroDeVertices);
        ControlMarcado enRecursion = new ControlMarcado(nroDeVertices);

        for (int i = 0; i < nroDeVertices; i++) {
            if (!visitados.estaMarcado(i)) {
                if (esAciclicoUtil(i, visitados, enRecursion)) {
                    return false; // Se encontró un ciclo
                }
            }
        }
        return true; // No se encontraron ciclos
    }

    // Método auxiliar para detectar ciclos en el grafo
    private boolean esAciclicoUtil(int v, ControlMarcado visitados, ControlMarcado enRecursion) throws ExcepcionPosicionDelVerticeInvalida {
        visitados.marcarVertice(v);
        enRecursion.marcarVertice(v);

        for (int adyacente : this.listaDeAdyacencias.get(v)) {
            if (!visitados.estaMarcado(adyacente) && esAciclicoUtil(adyacente, visitados, enRecursion)) {
                return true;
            } else if (enRecursion.estaMarcado(adyacente)) {
                return true;
            }
        }

        enRecursion.desmarcarVertice(v);
        return false;
    }

    // Método para verificar si el grafo es fuertemente conexo
    public boolean esFuertementeConexo() throws ExcepcionPosicionDelVerticeInvalida, ExcepcionesNroVerticeInvalido {
        int nroDeVertices = this.cantidadDeVertices();
        if (nroDeVertices == 0) {
            return true; // Un grafo vacío se considera fuertemente conexo
        }

        // Verificar si todos los vértices son alcanzables desde el primer vértice
        DFS dfsOriginal = new DFS(this, 0);
        if (!dfsOriginal.llegoATodos()) {
            return false;
        }

        // Invertir el grafo
        DiGrafo grafoInvertido = invertirGrafo();

        // Verificar si todos los vértices son alcanzables en el grafo invertido desde el primer vértice
        DFS dfsInvertido = new DFS(grafoInvertido, 0);
        return dfsInvertido.llegoATodos();
    }

    // Método para invertir el grafo
    private DiGrafo invertirGrafo() throws ExcepcionesNroVerticeInvalido {
        int nroDeVertices = this.cantidadDeVertices();
        DiGrafo grafoInvertido = new DiGrafo(nroDeVertices);
        for (int i = 0; i < nroDeVertices; i++) {
            for (int adyacente : this.listaDeAdyacencias.get(i)) {
                try {
                    grafoInvertido.insertarArista(adyacente, i);
                } catch (aristaYaExiste | ExcepcionPosicionDelVerticeInvalida e) {
                    // Esta excepción no debería ocurrir, ya que estamos construyendo un nuevo grafo
                }
            }
        }
        return grafoInvertido;
    }
    public boolean[][] algoritmoDeWarshall() throws ExcepcionPosicionDelVerticeInvalida {
        int n = this.cantidadDeVertices();
        boolean[][] cierreTransitivo = new boolean[n][n];

        // Inicialización de la matriz de cierre transitivo con la matriz de adyacencia
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cierreTransitivo[i][j] = this.existeAdyacencia(i, j);
            }
        }

        // Aplicación del algoritmo de Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    cierreTransitivo[i][j] = cierreTransitivo[i][j] || (cierreTransitivo[i][k] && cierreTransitivo[k][j]);
                }
            }
        }

        return cierreTransitivo;
    }

}
