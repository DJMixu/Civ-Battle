package com.example.civbattle;

import java.awt.*;

abstract class Jednostka extends Obiekt {
    int zycie;

    public Jednostka(int id, int pX, int pY) {
        super(id, pX, pY);
    }

    public Jednostka(int id, Point pP) {
        super(id, pP);
    }


    abstract void ruch();
}