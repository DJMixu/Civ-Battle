package com.example.civbattle;

class Osada extends Jednostka {
    public Osada(int id, int[] pozycja) {
        super(id, pozycja);
    }

    @Override
    void ruch() {
        if(this.zycie<50 && this.zycie >= 45)
            this.zycie = 50;
        else if (this.zycie<45)
            this.zycie+=5;
    }
}