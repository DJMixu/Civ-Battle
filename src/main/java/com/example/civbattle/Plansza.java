package com.example.civbattle;

public class Plansza {
    private Obiekt[][] plansza;
    public Plansza(int px , int py){
        plansza = new Obiekt[px][py];
    }

    public Obiekt zwrocPole(int x , int y){
        return plansza[x][y];
    }
    public String dodajObiekt(int x , int y , Obiekt obiekt){
        plansza[x][y] = obiekt;
        return "dodano" + plansza[x][y].toString();
    }
}
