package com.example.civbattle;

import java.awt.*;

class Barbarzynca extends Jednostka {
    private final String logoPath = "images/barbarzynca.png";

    public Barbarzynca(int id, int pX, int pY) {
        super(id, pX, pY);
    }
    public Barbarzynca(int id ,Point pPozycja){
        super(id,pPozycja);
        pozycja = pPozycja;
        this.idCywilizacji=9;
    }

    void atak() {
    }

    @Override
    void ruch() {
    }
}
