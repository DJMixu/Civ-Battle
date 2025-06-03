package com.example.civbattle;

import java.awt.*;

/**
 * Klasa bazowa reprezentująca dowolny obiekt na planszy w grze.
 * <p>
 * Obiekty posiadają unikalny identyfikator oraz pozycję na siatce planszy.
 * Klasa ta może być dziedziczona przez bardziej wyspecjalizowane typy,
 * takie jak {@link Jednostka}, {@link Surowiec}, {@link Osada} itp.
 */
class Obiekt {

    /** Unikalny identyfikator obiektu w symulacji. */
    int id;

    /** Aktualna pozycja obiektu na planszy, wyrażona jako punkt (x, y). */
    public Point pozycja;

    /**
     * Tworzy nowy obiekt na podstawie współrzędnych X i Y.
     *
     * @param id identyfikator obiektu
     * @param pX współrzędna X pozycji
     * @param pY współrzędna Y pozycji
     */
    public Obiekt(int id, int pX, int pY) {
        this.id = id;
        this.pozycja = new Point(pX, pY);
    }

    /**
     * Tworzy nowy obiekt na podstawie gotowego punktu.
     *
     * @param id identyfikator obiektu
     * @param pP obiekt klasy {@link Point} reprezentujący pozycję na planszy
     */
    public Obiekt(int id, Point pP) {
        this.id = id;
        this.pozycja = pP;
    }
}
