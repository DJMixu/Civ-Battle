package com.example.civbattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Cywilizacja {
    private String[] nazwyCywilizacji = {
            "Egipt", "Rzym", "Grecja", "Chiny", "Persja", "Majowie", "Wikingowie", "Japonia"
    };
    public int idCywilizacji;
    public String nameCywilizacji;
    public int[] surowce = new int[]{50, 50, 50};
    List<Jednostka> jednostki = new ArrayList<>();
    List<Osada> osady = new ArrayList<>();


    public Cywilizacja(int pID){
        idCywilizacji = pID;
        nameCywilizacji = nazwyCywilizacji[pID];

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