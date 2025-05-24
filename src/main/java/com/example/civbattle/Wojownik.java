package com.example.civbattle;

class Wojownik extends Jednostka {
    int atak;

    public Wojownik(int id, int[] pozycja) {
        super(id, pozycja, 1);
        this.zycie = 20;
        this.atak = 10;
    }

    void atak() {
    }

    @Override
    void ruch() {
    }
}