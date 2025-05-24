package com.example.civbattle;

class Osadnik extends Jednostka {
    private final String imagePath;

    public Osadnik(int id, int pX, int pY) {
        super(id, pX, pY);
        this.imagePath = "images/osadnik.png";
    }

    void zbierajSurowce() {
    }

    void zakładajOsadę() {
    }

    @Override
    void ruch() {
    }
}
