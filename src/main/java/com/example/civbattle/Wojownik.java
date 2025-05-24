package com.example.civbattle;

class Wojownik extends Jednostka {
    private final String imagePath;
    int atak;

    public Wojownik(int id, int pX , int pY) {
        super(id, pX, pY);
        this.zycie = 20;
        this.atak = 10;
        this.imagePath = "images/wojownik.png";
    }

    void atak() {
    }

    @Override
    void ruch() {
    }
}
