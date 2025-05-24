package com.example.civbattle;

class Wojownik extends Jednostka {
    private final String logoPath;
    int atak;

    public Wojownik(int id, int pX, int pY) {
        super(id, pX, pY);
        this.zycie = 20;
        this.atak = 10;
        this.logoPath = "images/wojownik.png";
    }

    void atak() {
    }

    @Override
    public void ruch(Plansza plansza, Cywilizacja cywilizacja) {
    }
}
