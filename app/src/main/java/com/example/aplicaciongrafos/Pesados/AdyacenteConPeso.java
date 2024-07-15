package com.example.aplicaciongrafos.Pesados;

public class AdyacenteConPeso implements Comparable<AdyacenteConPeso> {
    private int indiceVertice;
    private double peso;

    public AdyacenteConPeso(int indiceVertice, double peso) {
        this.indiceVertice = indiceVertice;
        this.peso = peso;
    }

    public int getIndiceVertice() {
        return indiceVertice;
    }

    public void setIndiceVertice(int indiceVertice) {
        this.indiceVertice = indiceVertice;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    @Override
    public int compareTo(AdyacenteConPeso o) {
        return Double.compare(this.peso, o.peso);
    }
}
