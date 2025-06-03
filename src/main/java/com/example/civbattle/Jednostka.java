package com.example.civbattle;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstrakcyjna klasa reprezentująca jednostkę w grze.
 * Dziedziczy po klasie {@link Obiekt} i dodaje właściwości oraz metody
 * charakterystyczne dla jednostek należących do cywilizacji.
 * <p>
 * Przykładowe implementacje: {@link Osadnik}, {@link Wojownik}.
 */
public abstract class Jednostka extends Obiekt {

    /** Liczba punktów życia jednostki. Po osiągnięciu 0 jednostka ginie. */
    int zycie;

    /** Identyfikator cywilizacji, do której należy jednostka. */
    int idCywilizacji;

    /** Poprzednia pozycja jednostki na planszy (przydatna przy planowaniu ruchu). */
    public Point poprzedniaPozycja;

    /**
     * Konstruktor tworzący jednostkę na podstawie współrzędnych X i Y.
     *
     * @param id  identyfikator jednostki
     * @param pX  współrzędna X pozycji startowej
     * @param pY  współrzędna Y pozycji startowej
     */
    public Jednostka(int id, int pX, int pY) {
        super(id, pX, pY);
    }

    /**
     * Konstruktor tworzący jednostkę na podstawie punktu oraz ID cywilizacji.
     *
     * @param id              identyfikator jednostki
     * @param pP              pozycja jednostki
     * @param pidCywilizacji  identyfikator cywilizacji
     */
    public Jednostka(int id, Point pP, int pidCywilizacji) {
        super(id, pP);
        this.idCywilizacji = pidCywilizacji;
    }

    /**
     * Konstruktor tworzący jednostkę na podstawie punktu.
     * Cywilizacja nie jest przypisana przy tworzeniu.
     *
     * @param id  identyfikator jednostki
     * @param pP  pozycja jednostki
     */
    public Jednostka(int id, Point pP) {
        super(id, pP);
    }

    /**
     * Zwraca listę sąsiadujących pól wokół danej pozycji,
     * uwzględniając granice planszy.
     *
     * @param p          punkt odniesienia
     * @param szerokosc  szerokość planszy
     * @param wysokosc   wysokość planszy
     * @return lista sąsiadujących punktów z punktem {@code p}
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
     * Abstrakcyjna metoda odpowiedzialna za działanie jednostki w jednej turze.
     * Powinna być zaimplementowana w klasach dziedziczących.
     *
     * @param sim aktualna instancja symulacji
     * @return kod zwrotny:
     *         <ul>
     *             <li><b>0</b> – brak akcji</li>
     *             <li><b>1</b> – ruch jednostki</li>
     *             <li><b>2</b> – śmierć jednostki</li>
     *         </ul>
     */
    public abstract int ruch(Symulacja sim);
}
