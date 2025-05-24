package com.example.civbattle;

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
       listaCywilizacji = new Cywilizacja[plCywilizacji];
       for(int i=0; i<plCywilizacji;i++){
           listaCywilizacji[i] = new Cywilizacja(i);
           listaCywilizacji[i].dodajJednostkę(new Osadnik(1,1 , 1));
       }

       return 0;
    }

}
