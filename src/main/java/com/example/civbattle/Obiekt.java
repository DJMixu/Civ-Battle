package com.example.civbattle;

import java.awt.*;

/**
 * Klasa bazowa reprezentująca obiekt na planszy.
 * Każdy obiekt posiada unikalny identyfikator oraz pozycję na planszy.
 * Może być rozszerzana przez bardziej szczegółowe klasy, takie jak jednostki, surowce itp.
 */
class Obiekt {
    /** Unikalny identyfikator obiektu. */
    int id;

    /** Pozycja obiektu na planszy. */
    public Point pozycja;

    /**
     * Konstruktor obiektu na podstawie współrzędnych X i Y.
     *
     * @param id identyfikator obiektu
     * @param pX współrzędna X
     * @param pY współrzędna Y
     */
    public Obiekt(int id, int pX, int pY) {
        this.id = id;
        this.pozycja = new Point(pX, pY);
    }

    /**
     * Konstruktor obiektu na podstawie punktu.
     *
     * @param id identyfikator obiektu
     * @param pP punkt reprezentujący pozycję
     */
    public Obiekt(int id, Point pP) {
        this.id = id;
        this.pozycja = pP;
    }
}
