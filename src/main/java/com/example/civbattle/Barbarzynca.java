package com.example.civbattle;

import java.awt.*;

class Barbarzynca extends Jednostka {
    private final String logoPath = "images/barbarzynca.png";
    private int atak;

    public Barbarzynca(int id, int pX, int pY) {
        super(id, pX, pY);
    }
    public Barbarzynca(int id ,Point pPozycja){
        super(id,pPozycja);
        pozycja = pPozycja;
        this.idCywilizacji=9;
        this.zycie = 20;
        this.atak = 8;
    }

    int atak(Point pozycjaCel , Symulacja sim) {
         Jednostka cel = (Jednostka) sim.plansza.zwrocPole(pozycjaCel);
         if(cel.zycie>0){
             cel.zycie -= atak;
             return 5;
         }else {
             return 4;
         }
    }
    int smierc(Plansza plansza, Cywilizacja civ) {
        civ.licznikWojownikow--;
        System.out.println(this.id + "wojownik usuniety");
        plansza.usunObiekt(pozycja);
        return 2;
    }
    @Override
    public int ruch(Symulacja sim) {
        return 0;
    }
}
