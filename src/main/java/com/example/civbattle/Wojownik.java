package com.example.civbattle;

class Wojownik extends Jednostka {
    int atak;

    public Wojownik(int id, int[] pozycja, int życie, int atak) {
        super(id, pozycja, życie);
        this.atak = atak;
    }

    void atak() {
    }

    @Override
    void ruch() {
    }
}