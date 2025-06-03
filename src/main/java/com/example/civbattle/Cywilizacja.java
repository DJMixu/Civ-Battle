package com.example.civbattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Klasa reprezentująca cywilizację w grze.
 * <p>
 * Przechowuje informacje o zasobach, jednostkach oraz osadach należących do danej cywilizacji.
 * Każda cywilizacja posiada unikalny identyfikator oraz nazwę.
 */
public class Cywilizacja {

    /** Unikalny identyfikator cywilizacji. */
    public int idCywilizacji;

    /** Nazwa cywilizacji. */
    public String nameCywilizacji;

    /** Tablica z zasobami cywilizacji: [drewno, metal, kamień]. */
    public int[] surowce;

    /** Liczba wojowników należących do cywilizacji. */
    int licznikWojownikow;

    /** Liczba osadników należących do cywilizacji. */
    int licznikOsadnikow;

    /** Liczba osad należących do cywilizacji. */
    int licznikOsad;

    /** Lista jednostek należących do cywilizacji. */
    List<Jednostka> jednostki;

    /** Lista osad należących do cywilizacji. */
    List<Osada> osady;

    /**
     * Tworzy nową instancję cywilizacji z domyślnymi zasobami i pustymi listami jednostek i osad.
     *
     * @param pID identyfikator cywilizacji, używany do przypisania nazwy
     *            na podstawie zdefiniowanej listy nazw
     */
    public Cywilizacja(int pID) {
        idCywilizacji = pID;
        String[] nazwyCywilizacji = {
                "Egipt", "Rzym", "Grecja", "Chiny", "Persja",
                "Majowie", "Wikingowie", "Japonia", "Polska", "Barbarzyńcy"
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
     * Dodaje jednostkę do listy jednostek tej cywilizacji.
     *
     * @param j jednostka, która ma zostać dodana
     */
    void dodajJednostkę(Jednostka j) {
        jednostki.add(j);
    }

    /**
     * Wypisuje w konsoli szczegółowe dane o stanie cywilizacji:
     * liczby jednostek, osad, zasoby oraz zawartość list.
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
     * Zwraca tekstową reprezentację cywilizacji, zawierającą nazwę, jednostki, osady i zasoby.
     *
     * @return ciąg znaków opisujący cywilizację
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
