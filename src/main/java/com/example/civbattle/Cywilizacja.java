package com.example.civbattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa reprezentująca cywilizację w grze.
 * Przechowuje dane o jednostkach, osadach i zasobach danej cywilizacji.
 */
class Cywilizacja {
    /** Unikalny identyfikator cywilizacji. */
    public int idCywilizacji;

    /** Nazwa cywilizacji. */
    public String nameCywilizacji;

    /** Tablica z zasobami: [drewno, metal, kamień]. */
    public int[] surowce;

    /** Liczba wojowników w cywilizacji. */
    int licznikWojownikow;

    /** Liczba osadników w cywilizacji. */
    int licznikOsadnikow;

    /** Liczba osad w cywilizacji. */
    int licznikOsad;

    /** Lista wszystkich jednostek należących do cywilizacji. */
    List<Jednostka> jednostki;

    /** Lista wszystkich osad należących do cywilizacji. */
    List<Osada> osady;

    /**
     * Konstruktor cywilizacji.
     *
     * @param pID identyfikator cywilizacji (indeksowany zgodnie z listą nazw)
     */
    public Cywilizacja(int pID) {
        idCywilizacji = pID;
        String[] nazwyCywilizacji = {
                "Egipt", "Rzym", "Grecja", "Chiny", "Persja", "Majowie", "Wikingowie", "Japonia", "Polska", "Barbarzyńcy"
        };
        nameCywilizacji = nazwyCywilizacji[pID];
        licznikOsadnikow = 0;
        licznikWojownikow = 0;
        licznikOsad = 0;

        surowce = new int[]{50, 50, 50};  // drewno, metal, kamień
        jednostki = new ArrayList<>();
        osady = new ArrayList<>();
    }

    /**
     * Dodaje jednostkę do listy jednostek cywilizacji.
     *
     * @param j jednostka do dodania
     */
    void dodajJednostkę(Jednostka j) {
        jednostki.add(j);
    }

    /**
     * Wypisuje szczegółowe informacje o stanie cywilizacji do konsoli.
     */
    public void wypiszDane() {
        System.out.println("///////////////////////");
        System.out.println("Nazwa: " + nameCywilizacji);
        System.out.println("Liczba wojowników: " + licznikWojownikow);
        System.out.println("Liczba osadników: " + licznikOsadnikow);
        System.out.println("Liczba osad: " + licznikOsad);
        System.out.println("Drewno: " + surowce[0]);
        System.out.println("Metal: " + surowce[1]);
        System.out.println("Kamień: " + surowce[2]);
        System.out.println("Jednostki: " + jednostki);
        System.out.println("Osady: " + osady);
        System.out.println("///////////////////////");
    }

    /**
     * Zwraca reprezentację tekstową obiektu.
     *
     * @return ciąg znaków reprezentujący stan cywilizacji
     */
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
