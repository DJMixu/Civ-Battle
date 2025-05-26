package com.example.civbattle;

import java.awt.*;
import java.util.List;

public class Osada extends Jednostka {
    private final String logoPath = "images/osada.png";

    public Osada(int id, Point pozycja, int civ) {
        super(id, pozycja, civ);
        this.zycie = 100;
    }

    void smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikOsad--;
        System.out.println(this.id + " osada usunięta");
        plansza.usunObiekt(pozycja);
        civ.jednostki.remove(this);
    }

    public int produkuj(int typ, Symulacja sim) {
        List<Point> sasiedzi = getSasiedzi(this.pozycja, sim.iSymulacjaX, sim.iSymulacjaY);
        Point pustePole = sasiedzi.stream()
                .filter(p -> sim.plansza.zwrocPole(p) == null)
                .findFirst()
                .orElse(null);

        if (pustePole == null) return 3; // brak miejsca

        Jednostka nowa;
        if (typ == 1) { // Osadnik
            nowa = new Osadnik(Symulacja.iSymlacjaLicznikID++, pustePole, this.idCywilizacji);
            sim.listaCywilizacji[this.idCywilizacji].licznikOsadnikow++;
        } else { // Wojownik
            nowa = new Wojownik(Symulacja.iSymlacjaLicznikID++, pustePole, this.idCywilizacji);
            sim.listaCywilizacji[this.idCywilizacji].licznikWojownikow++;
        }

        sim.plansza.dodajObiekt(pustePole, nowa);
        sim.listaCywilizacji[this.idCywilizacji].jednostki.add(nowa);
        for (int i = 0; i < 3; i++)
            sim.listaCywilizacji[this.idCywilizacji].surowce[i] -= 400;

        return 3;
    }

    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];

        if (this.zycie < 50) {
            this.zycie = Math.min(50, this.zycie + 5);
        }

        if (this.zycie <= 0) {
            smierc(sim.plansza, civ);
            return 2;
        }

        boolean maSurowce = civ.surowce[0] >= 800 && civ.surowce[1] >= 800 && civ.surowce[2] >= 800;
        if (maSurowce && civ.licznikOsadnikow < civ.licznikOsad) {
            if (civ.licznikOsadnikow < civ.licznikWojownikow) {
                produkuj(1, sim); // Osadnik
            } else {
                produkuj(2, sim); // Wojownik
            }
        }

        return 0;
    }
}
