package com.example.civbattle;

public class Osada extends Jednostka {

    public Osada(int id, int pX, int pY) {
        super(id, pX, pY);
    }

    @Override
    void ruch() {
        if(this.zycie<50 && this.zycie >= 45)
            this.zycie = 50;
        else if (this.zycie<45)
            this.zycie+=5;
    }
}