package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Symulacja {


    public Cywilizacja[] listaCywilizacji;
    public int iSymulacjaX , iSymulacjaY , iLiczbaCywilizacji;
    public Random ziarno;
    public Plansza plansza;
    //Domyślny start symulacji
    public Symulacja(int pX, int pY, int plCiv, String pSeed) {
        System.out.println(startSymulacji(pX, pY, plCiv, pSeed) == 0
                ? "Symulacja wystartowała"
                : "Symulacja nie wystartowała z błędem");
    }

    // Konstruktor z domyślnym seed
    public Symulacja(int pX, int pY, int plCiv) {
        this(pX, pY, plCiv, "defaultSeed");
    }

    // Konstruktor z domyślną liczbą cywilizacji i seedem
    public Symulacja(int pX, int pY) {
        this(pX, pY, 3, "defaultSeed"); // np. 3 domyślne cywilizacje
    }

    // Konstruktor z domyślnymi wszystkimi parametrami
    public Symulacja() {
        this(20, 20, 3, "defaultSeed");
    }

    public int startSymulacji(int pX , int pY , int plCywilizacji, String pSeed){
       ziarno = new Random(pSeed.hashCode());
       iSymulacjaX = pX;
       iSymulacjaY = pY;
       iLiczbaCywilizacji = plCywilizacji;
       plansza = new Plansza(iSymulacjaX,iSymulacjaY);
       listaCywilizacji = new Cywilizacja[iLiczbaCywilizacji];

       return 0;
    }
    public Point generujPozycje(){
        Point pozycja = new Point((ziarno.nextInt((iSymulacjaX-2))+1),(ziarno.nextInt((iSymulacjaY-2))+1));
        if(plansza.zwrocPole(pozycja) == null){
            if(obiektyWZasiegu(pozycja,3)==null)
                return pozycja;
            else return generujPozycje();
        }else
            return generujPozycje();
    }
    private void generujStart(){
        for(int i=0; i<iLiczbaCywilizacji;i++){
            listaCywilizacji[i] = new Cywilizacja(i);
            Point pozycja = generujPozycje();
            listaCywilizacji[i].dodajJednostkę(new Osadnik(1,pozycja));
            listaCywilizacji[i].dodajJednostkę(new Wojownik(2,(pozycja.x<iSymulacjaX/2 ? pozycja.x+1 : pozycja.x-1) , pozycja.y));
        }

    }
    public List<Obiekt> obiektyWZasiegu( Point punkt, int zasieg) {
        List<Obiekt> znalezione = new ArrayList<>();

        for (int dx = -zasieg; dx <= zasieg; dx++) {
            for (int dy = -zasieg; dy <= zasieg; dy++) {
                int nx = punkt.x + dx;
                int ny = punkt.y + dy;
                if (nx >= 0 && nx < iSymulacjaX && ny >= 0 && ny < iSymulacjaY) {
                    Obiekt o = plansza.zwrocPole(nx,ny);
                    if (o != null) {
                        znalezione.add(o);
                    }
                }
            }
        }
        return znalezione;
    }


}
