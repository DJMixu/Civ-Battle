package com.example.civbattle;

import java.awt.*;
import java.util.List;

class Osadnik extends Jednostka {
    private final String logoPath = "images/osadnik.png";

    public Osadnik(int id, Point pozycja) {
        super(id, pozycja);
        this.zycie = 20;
    }

    void zbierajSurowce(Plansza plansza) {
    }

    int zakładajOsadę(Plansza plansza, Cywilizacja civ) {
        smierc(plansza, civ);
        plansza.dodajObiekt(pozycja, new Osada(Symulacja.iSymlacjaLicznikID++, pozycja, civ.idCywilizacji));
        return 2;
    }

    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikOsadnikow--;
        System.out.println(this.id + "osadnik usuniety");
        plansza.usunObiekt(pozycja);
        return 2;
    }

    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];
        if(!(this.zycie > 0))
            return smierc(sim.plansza,civ);
        if (civ.surowce[0] >= 200 && civ.surowce[1] >= 200 && civ.surowce[2] >= 200 && civ.licznikOsad < civ.licznikWojownikow) {
            return zakładajOsadę(sim.plansza, civ);
        }
        List<Obiekt> znalezione = Symulacja.obiektyWZasiegu(pozycja, 3, sim.plansza);
        return 0;
    }
}
