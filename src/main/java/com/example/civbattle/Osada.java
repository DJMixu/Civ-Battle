package com.example.civbattle;

import java.awt.*;

public class Osada extends Jednostka {
    private final String logoPath= "images/osada.png";

    public Osada(int id ,Point pozycja , int civ) {
        super(id , pozycja, civ);
    }
    void smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikOsad--;
        System.out.println(this.id + "osadnik usuniety");
        plansza.usunObiekt(pozycja);
    }

    @Override
    public int ruch(Symulacja sim) {
        if (this.zycie < 50 && this.zycie >= 45)
            this.zycie = 50;
        else if (this.zycie < 45)
            this.zycie += 5;

        return 0;
    }
}
