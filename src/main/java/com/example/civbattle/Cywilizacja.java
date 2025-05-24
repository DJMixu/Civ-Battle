package com.example.civbattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Cywilizacja {
    private String[] nazwyCywilizacji = {
            "Egipt", "Rzym", "Grecja", "Chiny", "Persja", "Majowie", "Wikingowie", "Japonia" , "Polska" , "Barbażyńcy"
    };
    public int idCywilizacji;
    public String nameCywilizacji;
    public int[] surowce = new int[]{50, 50, 50};
    int licznikWojownikow , licznikOsadnikow , licznikOsad;
    List<Jednostka> jednostki = new ArrayList<>();
    List<Osada> osady = new ArrayList<>();


    public Cywilizacja(int pID) {
        idCywilizacji = pID;
        nameCywilizacji = nazwyCywilizacji[pID];
        licznikOsadnikow = 0;
        licznikWojownikow = 0;
        licznikOsad = 0;

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