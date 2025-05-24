package com.example.civbattle;

import java.awt.*;
import java.util.List;

class Osadnik extends Jednostka {
    private final String logoPath;

    public Osadnik(int id, int pX, int pY) {
        super(id, pX, pY);
        this.logoPath = "images/osadnik.png";
    }

    public Osadnik(int id, Point pozycja) {
        super(id, pozycja);
        this.logoPath = "images/osadnik.png";
    }

    void zbierajSurowce(Plansza plansza) {
    }

    void zakładajOsadę(Plansza plansza , Cywilizacja civ) {
        civ.licznikOsadnikow--;
        System.out.println(this.id + "osadnik usuniety");
        civ.jednostki.remove(this);
        System.out.println(this.id + "osadnik usuniety");
        plansza.usunObiekt(pozycja);
        plansza.dodajObiekt(pozycja , new Osada(Symulacja.iSymlacjaLicznikID++, pozycja , civ.idCywilizacji));


    }

    @Override
    public void ruch(Plansza plansza, Cywilizacja civ) {
        if(civ.surowce[0] >= 2 && civ.surowce[1] >= 2 && civ.surowce[2] >= 2 && civ.licznikOsad<civ.licznikWojownikow) {
            zakładajOsadę(plansza, civ);
        }
        List<Obiekt> znalezione = Symulacja.obiektyWZasiegu(pozycja,3,plansza);

    }
}
