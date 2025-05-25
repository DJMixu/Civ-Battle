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
                "Egipt", "Rzym", "Grecja", "Chiny", "Persja", "Majowie", "Wikingowie", "Japonia", "Polska", "Barbażyńcy"
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