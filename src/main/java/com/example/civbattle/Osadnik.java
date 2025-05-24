package com.example.civbattle;

import java.awt.*;

class Osadnik extends Jednostka {


    public Osadnik(int id, int pX, int pY) {
        super(id, pX, pY);
    }

    public Osadnik(int id, Point pozycja) {
        super(id , pozycja);
    }


    void zbierajSurowce() {
    }

    void zakładajOsadę() {
    }

    @Override
    void ruch() {
    }
}