package com.example.civbattle;

import java.util.List;

class Cywilizacja {
    List<Jednostka> jednostki;
    List<Osada> osady;
    int[] surowce;

    void dodajJednostkę(Jednostka j) {
        jednostki.add(j);
    }
}