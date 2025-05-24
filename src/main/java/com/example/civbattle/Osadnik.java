package com.example.civbattle;

import java.awt.*;

class Osadnik extends Jednostka {
    private final String logoPath;

    public Osadnik(int id, int pX, int pY) {
        super(id, pX, pY);
        this.logoPath = "images/osadnik.png";
    }

    public Osadnik(int id, Point pozycja) {
        super(id, pozycja);
        this.logoPath = "images/osadnik.png";
    }

    void zbierajSurowce() {
    }

    void zakładajOsadę() {
    }

    @Override
    void ruch() {
    }
}
