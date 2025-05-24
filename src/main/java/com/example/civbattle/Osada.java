package com.example.civbattle;

public class Osada extends Jednostka {
    private final String logoPath;

    public Osada(int id, int pX, int pY) {
        super(id, pX, pY);
        this.logoPath = "images/osada.png";
    }

    @Override
    void ruch() {
        if(this.zycie<50 && this.zycie >= 45)
            this.zycie = 50;
        else if (this.zycie<45)
            this.zycie+=5;
    }
}
