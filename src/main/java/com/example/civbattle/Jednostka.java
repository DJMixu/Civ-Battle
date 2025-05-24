package com.example.civbattle;

abstract class Jednostka extends Obiekt {
    int zycie;

    public Jednostka(int id, int pX, int pY) {
        super(id, pX, pY);
    }


    abstract void ruch();
}