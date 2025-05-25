package com.example.civbattle;

import java.awt.*;
import java.util.List;

public class Osada extends Jednostka {
    private final String logoPath = "images/osada.png";

    public Osada(int id, Point pozycja, int civ) {
        super(id, pozycja, civ);
    }

    void smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikOsad--;
        System.out.println(this.id + "osadnik usuniety");
        plansza.usunObiekt(pozycja);
    }

    public void produkuj(int typ, Symulacja sim) {
        List<Point> sasiedzi = getSasiedzi(this.pozycja, sim.iSymulacjaX, sim.iSymulacjaY);
        Point pustePole = new Point(-1,-1);
        for (Point point : sasiedzi) {
            if(sim.plansza.zwrocPole(point)==null){
                pustePole = point;
                break;
            }
        }
        if(pustePole.x != -1){
            if(typ==1){
                Jednostka os = new Osadnik(Symulacja.iSymlacjaLicznikID , pustePole ,  this.idCywilizacji);
                sim.plansza.dodajObiekt(pustePole , os);
                sim.listaCywilizacji[this.idCywilizacji].jednostki.add(os);
            }else{
                Jednostka os = new Wojownik(Symulacja.iSymlacjaLicznikID , pustePole ,  this.idCywilizacji);
                sim.plansza.dodajObiekt(pustePole , os);
                sim.listaCywilizacji[this.idCywilizacji].jednostki.add(os);
            }
        }

    }

    @Override
    public int ruch(Symulacja sim) {
        Cywilizacja civ = sim.listaCywilizacji[this.idCywilizacji];
        if (this.zycie < 50 && this.zycie >= 45)
            this.zycie = 50;
        else if (this.zycie < 45)
            this.zycie += 5;
        if (!(zycie > 0))
            smierc(sim.plansza, civ);
        if (civ.surowce[0] >= 1000 && civ.surowce[1] >= 1000 && civ.surowce[2] >= 1000 && civ.licznikOsadnikow < civ.licznikOsad) {
            if (civ.licznikOsadnikow < civ.licznikWojownikow) {
                produkuj(1, sim);
            } else
                produkuj(2, sim);
        }

        return 0;
    }
}
