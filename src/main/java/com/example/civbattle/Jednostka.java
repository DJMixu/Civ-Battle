package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstrakcyjna klasa reprezentująca jednostkę w grze.
 * Dziedziczy po klasie Obiekt i dodaje logikę cywilizacyjną oraz zachowania jednostek.
 * Może być rozszerzana przez konkretne typy jednostek, takie jak Osadnik, Wojownik itp.
 */
abstract class Jednostka extends Obiekt {
    /** Ilość punktów życia jednostki. */
    int zycie;

    /** Identyfikator cywilizacji, do której należy jednostka. */
    int idCywilizacji;

    /** Poprzednia pozycja jednostki na planszy. */
    public Point poprzedniaPozycja;

    /**
     * Konstruktor jednostki na podstawie współrzędnych X i Y.
     *
     * @param id identyfikator jednostki
     * @param pX współrzędna X
     * @param pY współrzędna Y
     */
    public Jednostka(int id, int pX, int pY) {
        super(id, pX, pY);
    }

    /**
     * Konstruktor jednostki na podstawie punktu i identyfikatora cywilizacji.
     *
     * @param id identyfikator jednostki
     * @param pP pozycja jednostki
     * @param pidCywilizacji identyfikator cywilizacji
     */
    public Jednostka(int id, Point pP, int pidCywilizacji) {
        super(id, pP);
        this.idCywilizacji = pidCywilizacji;
    }

    /**
     * Konstruktor jednostki na podstawie punktu (bez przypisania do cywilizacji).
     *
     * @param id identyfikator jednostki
     * @param pP pozycja jednostki
     */
    public Jednostka(int id, Point pP) {
        super(id, pP);
    }

    /**
     * Zwraca listę sąsiadujących pól wokół danej pozycji na planszy.
     *
     * @param p          punkt odniesienia
     * @param szerokosc  szerokość planszy
     * @param wysokosc   wysokość planszy
     * @return lista punktów sąsiadujących z punktem {@code p}
     */
    public List<Point> getSasiedzi(Point p, int szerokosc, int wysokosc) {
        List<Point> wynik = new ArrayList<>();
        int[] dx = {-1, 0, 1};
        int[] dy = {-1, 0, 1};

        for (int x : dx) {
            for (int y : dy) {
                if (x == 0 && y == 0) continue;

                int nx = p.x + x;
                int ny = p.y + y;

                if (nx >= 0 && nx < szerokosc && ny >= 0 && ny < wysokosc) {
                    wynik.add(new Point(nx, ny));
                }
            }
        }

        return wynik;
    }

    /**
     * Metoda abstrakcyjna wykonująca ruch jednostki w danej turze symulacji.
     *
     * @param sim aktualna instancja symulacji
     * @return kod zwrotny oznaczający wynik ruchu
     */
    public abstract int ruch(Symulacja sim);
}
