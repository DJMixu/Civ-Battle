package com.example.civbattle;

class Barbarzynca extends Jednostka {
    private final String imagePath;

    public Barbarzynca(int id, int pX, int pY) {
        super(id, pX, pY);
        this.imagePath = "images/barbarzynca.png";
    }

    void atak() {
    }

    @Override
    void ruch() {
    }
}
