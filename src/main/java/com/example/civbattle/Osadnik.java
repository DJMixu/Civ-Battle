package com.example.civbattle;

import java.awt.*;

class Osadnik extends Jednostka {
    private final String imagePath;

    public Osadnik(int id, int pX, int pY) {
        super(id, pX, pY);
        this.imagePath = "images/osadnik.png";
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
