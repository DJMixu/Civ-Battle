package com.example.civbattle;

import java.awt.*;

public class Osada extends Jednostka {
    private final String logoPath;

    public Osada(int id ,Point pozycja , int civ) {
        super(id , pozycja, civ);
        this.logoPath = "images/osada.png";
    }

    @Override
    public void ruch(Plansza plansza, Cywilizacja cywilizacja) {
        if (this.zycie < 50 && this.zycie >= 45)
            this.zycie = 50;
        else if (this.zycie < 45)
            this.zycie += 5;
    }
}
