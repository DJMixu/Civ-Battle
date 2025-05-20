package com.example.civbattle;

abstract class Jednostka extends Obiekt {
    int zycie;

    public Jednostka(int id, int[] pozycja) {
        super(id, pozycja);
        this.zycie = 10;
    }

    abstract void ruch();
}