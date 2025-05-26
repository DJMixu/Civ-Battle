package com.example.civbattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Cywilizacja {
    public int idCywilizacji;
    public String nameCywilizacji;
    public int[] surowce;
    int licznikWojownikow , licznikOsadnikow , licznikOsad;
    List<Jednostka> jednostki;
    List<Osada> osady;


    public Cywilizacja(int pID) {
        idCywilizacji = pID;
        String[] nazwyCywilizacji = {
                "Egipt", "Rzym", "Grecja", "Chiny", "Persja", "Majowie", "Wikingowie", "Japonia", "Polska", "Barbarzyńcy"
        };
        nameCywilizacji = nazwyCywilizacji[pID];
        licznikOsadnikow = 0;
        licznikWojownikow = 0;
        licznikOsad = 0;

        surowce = new int[]{50, 50, 50};
        jednostki = new ArrayList<>();
        osady = new ArrayList<>();
    }

    void dodajJednostkę(Jednostka j) {
        jednostki.add(j);
    }
    public void wypiszDane(){
        System.out.println("///////////////////////");
        System.out.println("Nazwa" + nameCywilizacji);
        System.out.println("licznik wojownikow" + licznikWojownikow);
        System.out.println("licznik osadnikow" + licznikOsadnikow);
        System.out.println("licznik osad" + licznikOsad);
        System.out.println("drewno" + surowce[0]);
        System.out.println("metal" + surowce[1]);
        System.out.println("kamien" + surowce[2]);
        System.out.println("jednostki " + jednostki.toString());
        System.out.println("osady " + osady.toString());
        System.out.println("///////////////////////");
    }

    @Override
    public String toString() {
        return "Cywilizacja{" +
                "ID Cywilizacji=" + idCywilizacji +
                ", Nazwa Cywilizacji='" + nameCywilizacji + '\'' +
                ", Jednostki=" + jednostki +
                ", Osady=" + osady +
                ", Surowce=" + Arrays.toString(surowce) +
                '}';
    }
}