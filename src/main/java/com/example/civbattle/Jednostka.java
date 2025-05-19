package com.example.civbattle;

abstract class Jednostka extends Obiekt {
    int życie;

    public Jednostka(int id, int[] pozycja, int życie) {
        super(id, pozycja);
        this.życie = życie;
    }

    abstract void ruch();
}