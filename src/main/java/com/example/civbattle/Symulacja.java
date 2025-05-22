package com.example.civbattle;

import java.util.Random;

public class Symulacja {
    public int iSymulacjaX , iSymulacjaY , iLiczbaCywilizacji;
    public Symulacja (){
        startSymulacji(20,20,4,"Ala ma kota");

    }
    public int startSymulacji(int pX , int pY , int plCywilizacji, String pSeed){
       Random ziarno = new Random(pSeed.hashCode());
       iSymulacjaX = pX;
       iSymulacjaY = pY;
       iLiczbaCywilizacji = plCywilizacji;
       Plansza plansza = new Plansza(iSymulacjaX,iSymulacjaY);

        return 0;

    }

}
