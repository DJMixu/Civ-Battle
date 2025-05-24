package com.example.civbattle;

class Barbarzynca extends Jednostka {
    private final String logoPath;

    public Barbarzynca(int id, int pX, int pY) {
        super(id, pX, pY);
        this.logoPath = "images/barbarzynca.png";
    }

    void atak() {
    }

    @Override
    void ruch() {
    }
}
