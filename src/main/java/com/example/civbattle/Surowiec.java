package com.example.civbattle;

import java.awt.*;

/**
 * Klasa reprezentująca surowiec umieszczony na planszy.
 * Surowiec posiada jakość oraz typ (kamień, drewno, złoto), które wpływają na jego wartość.
 */
public class Surowiec extends Obiekt {

    /**
     * Tablica przechowująca wartości danego surowca w formacie: [kamień, drewno, złoto].
     */
    int[] wartosciSurowca = {0, 0, 0};

    /**
     * Typ surowca:
     * <ul>
     *     <li>0 - kamień</li>
     *     <li>1 - drewno</li>
     *     <li>2 - złoto</li>
     * </ul>
     */
    public int pTyp;

    /**
     * Konstruktor tworzący surowiec na podstawie identyfikatora i współrzędnych.
     * @param id identyfikator surowca
     * @param pX współrzędna X na planszy
     * @param pY współrzędna Y na planszy
     */
    public Surowiec(int id, int pX, int pY) {
        super(id, pX, pY);
    }

    /**
     * Konstruktor tworzący surowiec na podstawie pozycji, jakości i typu.
     * Automatycznie generuje wartości surowca.
     * @param id identyfikator surowca
     * @param pP pozycja na planszy jako Point
     * @param pJakosc jakość surowca (np. 1-10)
     * @param pTyp typ surowca: 0=kamień, 1=drewno, 2=złoto
     */
    public Surowiec(int id, Point pP, int pJakosc, int pTyp) {
        super(id, pP);
        this.pTyp = pTyp;
        generujWartosci(pJakosc, pTyp);
    }

    /**
     * Generuje wartości surowca w zależności od jego jakości i typu.
     * Każdy surowiec początkowo ma równą wartość w każdej kategorii (jakość * 10),
     * jednak typowy surowiec ma swoją główną wartość potrojoną.
     * @param pJakosc jakość surowca
     * @param pTyp typ surowca: 0=kamień, 1=drewno, 2=złoto
     */
    public void generujWartosci(int pJakosc, int pTyp) {
        wartosciSurowca[0] = pJakosc * 10;
        wartosciSurowca[1] = pJakosc * 10;
        wartosciSurowca[2] = pJakosc * 10;
        wartosciSurowca[pTyp] = wartosciSurowca[pTyp] * 3;
    }
}
